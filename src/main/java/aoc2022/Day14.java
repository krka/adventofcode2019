package aoc2022;

import util.Day;
import util.Util;
import util.Vec2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day14 implements Day {
  private final List<String> input;
  private final Set<Vec2> grid;
  private final long maxY;
  private final int startSize;

  public Day14(String name) {
    input = Util.readResource(name);
    grid = new HashSet<>();
    for (String s : input) {
      if (s.isEmpty()) {
        continue;
      }
      final String[] parts = s.split(" \\-> ");
      Vec2 prev = null;
      for (String part : parts) {
        final Vec2 v = Vec2.parse(part);
        if (prev != null) {
          for (Vec2 x = prev; !x.equals(v); x = x.add(v.sub(x).signum())) {
            grid.add(x);
          }
          grid.add(v);
        }
        prev = v;
      }
    }
    maxY = grid.stream().mapToLong(Vec2::getY).max().getAsLong();
    startSize = grid.size();
  }

  @Override
  public long solvePart1() {
    fill(Vec2.of(500, 0));
    return grid.size() - startSize;
  }

  @Override
  public long solvePart2() {
    fill2(Vec2.of(500, 0));
    return grid.size() - startSize;
  }

  private boolean fill(Vec2 sv) {
    if (sv.getY() > maxY) {
      return false;
    }

    final Vec2 nextDown = sv.add(Vec2.SOUTH);
    final Vec2 nextDownLeft = nextDown.add(Vec2.WEST);
    final Vec2 nextDownRight = nextDown.add(Vec2.EAST);

    boolean downFilled = grid.contains(nextDown) || fill(nextDown);
    boolean leftFilled = downFilled && (grid.contains(nextDownLeft) || fill(nextDownLeft));
    boolean rightFilled = leftFilled && (grid.contains(nextDownRight) || fill(nextDownRight));

    if (rightFilled) {
      grid.add(sv);
      return true;
    } else {
      return false;
    }
  }

  private boolean fill2(Vec2 sv) {
    final Vec2 nextDown = sv.add(Vec2.SOUTH);
    final Vec2 nextDownLeft = nextDown.add(Vec2.WEST);
    final Vec2 nextDownRight = nextDown.add(Vec2.EAST);

    boolean downFilled = contains2(nextDown) || fill2(nextDown);
    boolean leftFilled = downFilled && (contains2(nextDownLeft) || fill2(nextDownLeft));
    boolean rightFilled = leftFilled && (contains2(nextDownRight) || fill2(nextDownRight));

    if (rightFilled) {
      grid.add(sv);
      return true;
    } else {
      return false;
    }
  }

  private boolean contains2(Vec2 v) {
    return grid.contains(v) || v.getY() == maxY + 2;
  }
}


