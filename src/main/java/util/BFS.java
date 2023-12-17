package util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BFS<T> {
  private final Queue<Node<T>> queue;
  private final Set<T> visited = new HashSet<>();
  private final List<T> starts;
  private final Predicate<T> predicate;
  private final Function<T, Stream<T>> edgeFunction;

  public BFS(List<T> starts, Predicate<T> predicate, Function<T, Stream<T>> edgeFunction, Comparator<Node<T>> comparator) {
    this.starts = starts;
    this.predicate = predicate;
    this.edgeFunction = edgeFunction;
    if (comparator != null) {
      queue = new PriorityQueue<>(comparator);
    } else {
      queue = new ArrayDeque<>();
    }
  }

  public static<T> BFSBuilder<T> newBFS(Class<T> clazz) {
    return new BFSBuilder<>();
  }

  public static<T> BFSBuilder<T> newBFS(List<T> starts) {
    return new BFSBuilder<T>().withStarts(starts);
  }

  public static<T> BFSBuilder<T> newBFS(T start) {
    return new BFSBuilder<T>().withStart(start);
  }

  public static<T> BFSBuilder<T> newBFS(T... start) {
    return new BFSBuilder<T>().withStarts(List.of(start));
  }

  public Node<T> run() {
    queue.clear();
    visited.clear();
    for (T start : starts) {
      queue.add(new Node<>(start));
    }
    //long t1 = System.currentTimeMillis();
    while (true) {
      final Node<T> cur = queue.poll();
      if (cur == null || predicate.test(cur.node)) {
        //System.out.println("BFS runtime: " + (System.currentTimeMillis() - t1));
        return cur;
      }

      if (visited.add(cur.node)) {
        edgeFunction.apply(cur.node)
                .forEach(newPos -> queue.add(cur.to(newPos)));
      }
    }
  }

  public Set<T> visited() {
    return visited;
  }

  public static class BFSBuilder<T> {

    private List<T> starts;
    private Function<T, Stream<T>> edgeFunction;
    private Predicate<T> predicate = value -> false;
    private Comparator<Node<T>> comparator = null;

    private BFSBuilder() {
    }

    public BFSBuilder<T> withStarts(List<T> starts) {
      this.starts = starts;
      return this;
    }

    public BFSBuilder<T> withStart(T start) {
      this.starts = List.of(start);
      return this;
    }

    public BFSBuilder<T> withTarget(Predicate<T> predicate) {
      this.predicate = predicate;
      return this;
    }

    public BFSBuilder<T> withEdgeFunction(Function<T, Stream<T>> edgeFunction) {
      this.edgeFunction = edgeFunction;
      return this;
    }

    public BFSBuilder<T> withCostFunction(Comparator<T> comparator) {
      this.comparator = Comparator.comparing(Node::getNode, comparator);
      return this;
    }

    public BFS<T> build() {
      return new BFS<>(starts, predicate, edgeFunction, comparator);
    }
  }

  public static class Node<T> {
    private final T node;
    private final Node<T> prev;
    private final int steps;

    public Node(T node) {
      this.node = node;
      this.prev = null;
      this.steps = 0;
    }

    public Node(T node, Node<T> prev) {
      this.node = node;
      this.prev = prev;
      this.steps = prev.steps + 1;
    }

    public T getNode() {
      return node;
    }

    public Node<T> getPrev() {
      return prev;
    }

    public int getSteps() {
      return steps;
    }

    public List<T> getPath() {
      final List<T> result = new ArrayList<>();
      Node<T> cur = this;
      while (cur != null) {
        result.add(cur.node);
        cur = cur.prev;
      }
      Collections.reverse(result);
      return result;
    }
    @Override
    public String toString() {
      return node.toString();
    }

    public Node<T> to(T newPos) {
      return new Node<>(newPos, this);
    }
  }
}
