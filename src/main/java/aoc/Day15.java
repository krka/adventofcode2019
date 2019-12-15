package aoc;

import intcode.IntCode;
import util.Vector3;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Day15 {

  private final IntCode intCode;

  private final Set<Vector3> visited = new HashSet<>();
  private final Set<Vector3> blocked = new HashSet<>();
  private Vector3 start = Vector3.zero();
  private Vector3 goal;

  Vector3[] directions = new Vector3[]{
          null,
          Vector3.parse("0,-1,0"),
          Vector3.parse("0,1,0"),
          Vector3.parse("-1,0,0"),
          Vector3.parse("1,0,0")
  };

  int[] reverse = new int[]{-1, 2, 1, 4, 3};

  public Day15(String name) throws IOException {
    intCode = IntCode.fromResource(name);
  }

  public long part1() {
    scanMap(start);
    return bfs(start, goal);
  }

  private void scanMap(Vector3 pos) {
    for (int dir = 1; dir <= 4; dir++) {
      Vector3 nextPosition = pos.add(directions[dir]);
      if (visited.add(nextPosition)) {
        intCode.writeStdin(dir);
        intCode.run();
        int i = consumeStdout(nextPosition);
        if (i != 0) {
          scanMap(nextPosition);
          intCode.writeStdin(reverse[dir]);
          intCode.run();
          if (0 == consumeStdout(nextPosition)) {
            throw new RuntimeException("Could not backtrack");
          }
        }
      }
    }
  }

  private int consumeStdout(Vector3 nextPosition) {
    List<BigInteger> list = intCode.drainStdout();
    if (list.size() != 1) {
      throw new RuntimeException("Got too many outputs!");
    }
    for (BigInteger output : list) {
      if (output.equals(BigInteger.ZERO)) {
        blocked.add(nextPosition);
        return 0;
      } else if (output.equals(BigInteger.ONE)) {
        return 1;
      } else if (output.equals(BigInteger.TWO)) {
        goal = nextPosition;
        return 2;
      } else {
        throw new RuntimeException("Unexpected output: " + output);
      }
    }
    throw new RuntimeException("Unreachable");
  }

  public long part2() {
    scanMap(start);
    return bfs(goal, null);
  }

  private long bfs(Vector3 from, Vector3 to) {
    Set<Vector3> visited = new HashSet<>();
    Queue<Vector3> toVisit = new LinkedList<>();
    int iterations = 0;
    toVisit.add(from);
    visited.add(from);
    while (!toVisit.isEmpty()) {
      iterations++;
      int numNodes = toVisit.size();
      for (int i = 0; i < numNodes; i++) {
        Vector3 node = toVisit.poll();
        if (node.equals(to)) {
          return iterations - 1;
        }
        for (int j = 1; j <= 4; j++) {
          Vector3 next = node.add(directions[j]);
          if (visited.add(next)) {
            if (!blocked.contains(next)) {
              toVisit.add(next);
            }
          }
        }
      }
    }
    return iterations - 1;
  }

  public void print() {
    long minx = Integer.MAX_VALUE;
    long miny = Integer.MAX_VALUE;
    long maxx = Integer.MIN_VALUE;
    long maxy = Integer.MIN_VALUE;
    for (Vector3 v : visited) {
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
        } else if (visited.contains(v)) {
          c = '.';
        }
        System.out.print(c);
      }
      System.out.println();
    }
  }
}
