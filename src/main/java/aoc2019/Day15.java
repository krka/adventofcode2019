package aoc2019;

import intcode.IntCode;
import util.Vec3;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Day15 {

  private final IntCode intCode;

  private final Set<Vec3> visited = new HashSet<>();
  private final Set<Vec3> blocked = new HashSet<>();
  private Vec3 start = Vec3.zero();
  private Vec3 goal;

  Vec3[] directions = new Vec3[]{
          null,
          Vec3.parse("0,-1,0"),
          Vec3.parse("0,1,0"),
          Vec3.parse("-1,0,0"),
          Vec3.parse("1,0,0")
  };

  int[] reverse = new int[]{-1, 2, 1, 4, 3};

  public Day15(String name) {
    intCode = IntCode.fromResource(name);
  }

  public long part1() {
    scanMap(start);
    return bfs(start, goal);
  }

  private void scanMap(Vec3 pos) {
    for (int dir = 1; dir <= 4; dir++) {
      Vec3 nextPosition = pos.add(directions[dir]);
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

  private int consumeStdout(Vec3 nextPosition) {
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

  private long bfs(Vec3 from, Vec3 to) {
    Set<Vec3> visited = new HashSet<>();
    Queue<Vec3> toVisit = new LinkedList<>();
    int iterations = 0;
    toVisit.add(from);
    visited.add(from);
    while (!toVisit.isEmpty()) {
      iterations++;
      int numNodes = toVisit.size();
      for (int i = 0; i < numNodes; i++) {
        Vec3 node = toVisit.poll();
        if (node.equals(to)) {
          return iterations - 1;
        }
        for (int j = 1; j <= 4; j++) {
          Vec3 next = node.add(directions[j]);
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
    for (Vec3 v : visited) {
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
        Vec3 v = new Vec3(col, row, 0);
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
