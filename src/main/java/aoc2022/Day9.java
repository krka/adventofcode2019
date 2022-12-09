package aoc2022;

import util.Day;
import util.Util;
import util.Vec2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day9 implements Day {

  private final List<String> input;

  public Day9(String name) {
    input = Util.readResource(name);
  }

  @Override
  public long solvePart1() {
    return solve(1);
  }

  @Override
  public long solvePart2() {
    return solve(9);
  }

  private Vec2 getDir(char part) {
    switch (part) {
      case 'U': return Vec2.NORTH;
      case 'D': return Vec2.SOUTH;
      case 'L': return Vec2.WEST;
      case 'R': return Vec2.EAST;
    }
    throw new RuntimeException();
  }

  private int solve(int knots) {
    Set<Vec2> visited = new HashSet<>();
    Vec2 head = Vec2.zero();
    Vec2[] tails = new Vec2[knots];
    Arrays.fill(tails, Vec2.zero());
    visited.add(Vec2.zero());
    for (String s : input) {
      final String[] parts = s.split(" ");
      final int steps = Integer.parseInt(parts[1]);
      Vec2 dir = getDir(parts[0].charAt(0));
      for (int i = 0; i < steps; i++) {
        head = head.add(dir);
        Vec2 prev = head;
        for (int knot = 0; knot < knots; knot++) {
          Vec2 cur = tails[knot];
          final Vec2 diff = prev.sub(cur);
          if (diff.chessDistance() > 1) {
            cur = cur.add(diff.signum());
          }
          prev = cur;
          tails[knot] = prev;
        }
        visited.add(tails[knots - 1]);
      }
    }
    return visited.size();
  }
}


