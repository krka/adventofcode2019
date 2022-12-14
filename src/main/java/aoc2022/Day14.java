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
    final Vec2 start = Vec2.of(500, 0);
    while (true) {
      Vec2 pos = fill(start);
      if (pos == null) {
        return grid.size() - startSize;
      } else {
        grid.add(pos);
      }
    }
  }

  @Override
  public long solvePart2() {
    final Vec2 start = Vec2.of(500, 0);
    while (true) {
      Vec2 pos = fill2(start);
      grid.add(pos);
      if (pos.equals(start)) {
        return grid.size() - startSize;
      }
    }
  }

  private Vec2 fill(Vec2 sv) {
    while (true) {
      if (sv.getY() > maxY) {
        return null;
      }
      final Vec2 nextDown = sv.add(Vec2.SOUTH);
      final Vec2 nextDownLeft = nextDown.add(Vec2.WEST);
      final Vec2 nextDownRight = nextDown.add(Vec2.EAST);
      if (!grid.contains(nextDown)) {
        sv = nextDown;
      } else if (!grid.contains(nextDownLeft)) {
        sv = nextDownLeft;
      } else if (!grid.contains(nextDownRight)) {
        sv = nextDownRight;
      } else {
        return sv;
      }
    }
  }

  private Vec2 fill2(Vec2 sv) {
    while (true) {
      final Vec2 nextDown = sv.add(Vec2.SOUTH);
      final Vec2 nextDownLeft = nextDown.add(Vec2.WEST);
      final Vec2 nextDownRight = nextDown.add(Vec2.EAST);
      if (!contains2(nextDown)) {
        sv = nextDown;
      } else if (!contains2(nextDownLeft)) {
        sv = nextDownLeft;
      } else if (!contains2(nextDownRight)) {
        sv = nextDownRight;
      } else {
        return sv;
      }
    }
  }

  private boolean contains2(Vec2 v) {
    return grid.contains(v) || v.getY() == maxY + 2;
  }
}


