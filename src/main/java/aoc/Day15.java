package aoc;

import intcode.IntCode;
import util.Vector3;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Day15 {

  private final IntCode intCode;

  private final Map<Vector3, Vector3> comeFrom = new HashMap<>();
  private final Set<Vector3> blocked = new HashSet<>();
  private Vector3 start = Vector3.zero();
  private Vector3 position;
  private Vector3 goal;

  private final PriorityQueue<Vector3> edges = new PriorityQueue<>(
          Comparator.comparing(Vector3::manhattan)
  );

  Vector3[] directions = new Vector3[]{
          null,
          Vector3.parse("0,-1,0"),
          Vector3.parse("0,1,0"),
          Vector3.parse("-1,0,0"),
          Vector3.parse("1,0,0")
  };

  int[] reverse = new int[]{-1, 2, 1, 4, 3};

  int getDir(Vector3 vector) {
    for (int i = 1; i <= 4; i++) {
      if (directions[i].equals(vector)) {
        return i;
      }
    }
    throw new RuntimeException("Not a direction: " + vector);
  }

  public Day15(String name) throws IOException {
    intCode = IntCode.fromResource(name);
    position = Vector3.zero();
    comeFrom.put(start, start);
    edges.add(start);
  }

  public long part1() {
    scanMap();
    return bfs(start, goal);
  }

  private void scanMap() {
    while (!edges.isEmpty()) {
      Vector3 edge = edges.poll();

      ArrayList<Integer> res = new ArrayList<>();
      findPath(edge, res);

      Collections.reverse(res);
      walkTo(res);

      for (int i = 1; i <= 4; i++) {
        Vector3 dir = directions[i];
        Vector3 nextPosition = position.add(dir);
        if (!blocked.contains(nextPosition) && !comeFrom.containsKey(nextPosition)) {
          comeFrom.put(nextPosition, position);
          if (tryMove(i) == 0) {
            blocked.add(nextPosition);
          } else {
            edges.add(nextPosition);
            if (tryMove(reverse[i]) == 0) {
              throw new RuntimeException("Could not move back?");
            }
          }
        }
      }
      while (!position.equals(start)) {
        Vector3 from2 = comeFrom.get(position);
        if (0 == tryMove(getDir(from2.sub(position)))) {
          throw new RuntimeException("Could not move back? Weird");
        }
      }
    }
  }

  private void walkTo(ArrayList<Integer> res) {
    for (int dir : res) {
      int i = tryMove(dir);
      if (i == 0) {
        throw new RuntimeException("Unexpected");
      }
    }
  }


  private List<Integer> findPath(Vector3 node, ArrayList<Integer> res) {
    while (!node.equals(start)) {
      Vector3 from = comeFrom.get(node);
      res.add(getDir(node.sub(from)));
      node = from;
    }
    return res;
  }

  private int tryMove(int dir) {
    intCode.writeStdin(dir);
    intCode.run();

    Vector3 nextPosition = position.add(directions[dir]);

    List<BigInteger> list = intCode.drainStdout();
    if (list.size() != 1) {
      throw new RuntimeException("Got too many outputs!");
    }
    for (BigInteger output : list) {
      if (output.equals(BigInteger.ZERO)) {
        blocked.add(nextPosition);
        return 0;
      } else if (output.equals(BigInteger.ONE)) {
        position = nextPosition;
        return 1;
      } else if (output.equals(BigInteger.TWO)) {
        position = nextPosition;
        goal = nextPosition;
        return 2;
      } else {
        throw new RuntimeException("Unexpected output: " + output);
      }
    }
    throw new RuntimeException("Unreachable");
  }

  public long part2() {
    // Populate it
    part1();

    return bfs(goal, null);
  }

  private long bfs(Vector3 from, Vector3 to) {
    int max = 0;
    Map<Vector3, Integer> visited = new HashMap<>();
    Queue<Vector3> toVisit = new LinkedList<>();
    int loops = 0;
    toVisit.add(from);
    visited.put(from, 0);
    while (!toVisit.isEmpty()) {
      loops++;
      int iterations = toVisit.size();
      for (int i = 0; i < iterations; i++) {
        Vector3 node = toVisit.poll();
        if (node.equals(to)) {
          return max;
        }
        for (int j = 1; j <= 4; j++) {
          Vector3 next = node.add(directions[j]);
          if (!blocked.contains(next) && !visited.containsKey(next)) {
            int value = visited.get(node) + 1;
            max = Math.max(max, value);
            visited.put(next, value);
            toVisit.add(next);
          }
        }
      }
    }
    return max;
  }

  public void print() {
    long minx = Integer.MAX_VALUE;
    long miny = Integer.MAX_VALUE;
    long maxx = Integer.MIN_VALUE;
    long maxy = Integer.MIN_VALUE;
    for (Vector3 v : comeFrom.keySet()) {
      long x = v.getX();
      long y = v.getY();
      minx = Math.min(minx, x);
      miny = Math.min(miny, y);
      maxx = Math.max(maxx, x);
      maxy = Math.max(maxy, y);
    }

    for (long col = minx; col <= maxx; col++) {
      for (long row = miny; row <= maxy; row++) {
        char c = ' ';
        Vector3 v = new Vector3(col, row, 0);
        if (goal.equals(v)) {
          c = 'O';
        } else if (blocked.contains(v)) {
          c = '#';
        } else if (comeFrom.containsKey(v)) {
          c = '.';
        }
        System.out.print(c);
      }
      System.out.println();
    }
  }
}
