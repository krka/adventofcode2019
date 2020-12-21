package util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Bipartite<A, B> {

  private final List<A> left;
  private final List<B> right;

  private final int leftSize;
  private final int rightSize;
  private final int V;
  private final ArrayList<Set<Integer>> edges;

  private int cardinality;             // cardinality of current matching
  private int[] mate;                  // mate[v] =  w if v-w is an edge in current matching
  //         = -1 if v is not in current matching
  private boolean[] inMinVertexCover;  // inMinVertexCover[v] = true iff v is in min vertex cover
  private boolean[] marked;            // marked[v] = true iff v is reachable via alternating path
  private int[] edgeTo;                // edgeTo[v] = last edge on alternating path to v
  private final Map<A, Integer> lookupLeft;
  private final Map<B, Integer> lookupRight;

  public Bipartite(List<A> left, List<B> right, Map<A, ? extends Collection<B>> edges) {
    this.left = left;
    this.right = right;

    int i = 0;
    lookupLeft = new HashMap<>();
    for (A val : left) {
      if (null != lookupLeft.put(val, i++)) {
        throw new RuntimeException("Duplicate key in left: " + val);
      }
    }
    lookupRight = new HashMap<>();
    for (B val : right) {
      if (null != lookupRight.put(val, i++)) {
        throw new RuntimeException("Duplicate key in right: " + val);
      }
    }
    leftSize = left.size();
    rightSize = right.size();
    V = leftSize + rightSize;
    this.edges = new ArrayList<>();
    for (int j = 0; j < V; j++) {
      this.edges.add(new HashSet<>());
    }
    for (Map.Entry<A, ? extends Collection<B>> entry : edges.entrySet()) {
      A key = entry.getKey();
      Integer leftIndex = lookupLeft.get(key);
      if (leftIndex == null) {
        throw new RuntimeException("Found in edge but not in left side: " + entry.getKey());
      }
      Set<Integer> leftSet = this.edges.get(leftIndex);
      entry.getValue().stream().mapToInt(lookupRight::get).forEach(rightIndex -> {
        Set<Integer> rightSet = this.edges.get(rightIndex);
        rightSet.add(leftIndex);
        leftSet.add(rightIndex);
      });
    }

    // initialize empty matching
    mate = new int[V];
    for (int v = 0; v < V; v++) {
      mate[v] = -1;
    }

    marked = new boolean[V];
    edgeTo = new int[V];

    // alternating path algorithm
    while (hasAugmentingPath()) {

      // find one endpoint t in alternating path
      int t = -1;
      for (int v = 0; v < V; v++) {
        if (!isMatched(v) && edgeTo[v] != -1) {
          t = v;
          break;
        }
      }

      // update the matching according to alternating path in edgeTo[] array
      for (int v = t; v != -1; v = edgeTo[edgeTo[v]]) {
        int w = edgeTo[v];
        mate[v] = w;
        mate[w] = v;
      }
      cardinality++;
    }

    // find min vertex cover from marked[] array
    inMinVertexCover = new boolean[V];
    for (int v = 0; v < V; v++) {
      if (color(v) && !marked[v]) inMinVertexCover[v] = true;
      if (!color(v) && marked[v]) inMinVertexCover[v] = true;
    }
  }

  public static <A, B> Bipartite<A, B> from(List<A> left, List<B> right, Map<A, ? extends Collection<B>> edges) {
    return new Bipartite<A, B>(left, right, edges);
  }

  public B getRightMatch(A left) {
    Integer leftIndex = lookupLeft.get(left);
    if (leftIndex == null) {
      throw new RuntimeException("Could not find in left: " + left);
    }
    int rightIndex = mate[leftIndex];
    if (rightIndex != -1) {
      return right.get(rightIndex - leftSize);
    }
    return null;
  }

  public A getLeftMatch(B right) {
    Integer rightIndex = lookupRight.get(right);
    if (rightIndex == null) {
      throw new RuntimeException("Could not find in right: " + right);
    }
    int leftIndex = mate[rightIndex];
    if (leftIndex != -1) {
      return left.get(leftIndex);
    }
    return null;
  }

  /**
   * Returns the number of edges in a maximum matching.
   *
   * @return the number of edges in a maximum matching
   */
  public int size() {
    return cardinality;
  }

  /**
   * Returns true if the graph contains a perfect matching.
   * That is, the number of edges in a maximum matching is equal to one half
   * of the number of vertices in the graph (so that every vertex is matched).
   *
   * @return {@code true} if the graph contains a perfect matching;
   *         {@code false} otherwise
   */
  public boolean isPerfect() {
    return cardinality * 2 == V;
  }


  private boolean color(int v) {
    return v < leftSize;
  }

  private boolean isMatched(int v) {
    return mate[v] != -1;
  }

  private boolean hasAugmentingPath() {
    for (int v = 0; v < V; v++) {
      marked[v] = false;
      edgeTo[v] = -1;
    }

    // breadth-first search (starting from all unmatched vertices on one side of bipartition)
    Queue<Integer> queue = new ArrayDeque<>();
    for (int v = 0; v < V; v++) {
      if (color(v) && !isMatched(v)) {
        queue.add(v);
        marked[v] = true;
      }
    }

    // run BFS, stopping as soon as an alternating path is found
    while (!queue.isEmpty()) {
      int v = queue.poll();
      for (int w : edges.get(v)) {
        // either (1) forward edge not in matching or (2) backward edge in matching
        if (isResidualGraphEdge(v, w) && !marked[w]) {
          edgeTo[w] = v;
          marked[w] = true;
          if (!isMatched(w)) {
            return true;
          }
          queue.add(w);
        }
      }
    }

    return false;
  }

  // is the edge v-w a forward edge not in the matching or a reverse edge in the matching?
  private boolean isResidualGraphEdge(int v, int w) {
    if ((mate[v] != w) && color(v)) return true;
    if ((mate[v] == w) && !color(v)) return true;
    return false;
  }

  private int[] toArray(Map<B, Integer> lookupRight, Collection<B> value) {
    int[] arr = new int[value.size()];
    int i = 0;
    for (B val : value) {
      arr[i++] = lookupRight.get(val);
    }
    return arr;
  }

}
