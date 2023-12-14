package aoc2023;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 implements Day {
  public static final long MAXCYCLES = 1000000000L;
  private final List<List<String>> lines;
  private final Grid<Character> grid;

  public Day14(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    this.grid = Grid.from(lines.get(0), '#');
  }

  @Override
  public long solvePart1() {
    final Grid<Character> g = grid.duplicate();
    tilt(g, Vec2.NORTH);
    return load(g);
  }

  private static long load(Grid<Character> x) {
    final int rows = x.rows();
    return x.stream()
            .filter(e -> e.getValue() == 'O')
            .mapToLong(e -> (rows - e.getRow())).sum();
  }

  @Override
  public long solvePart2() {
    final Grid<Character> g = grid.duplicate();

    Map<Long, Long> cache = new HashMap<>();

    long cycle = 0;
    while (true) {
      if (cycle == MAXCYCLES) {
        return load(g);
      }
      final long hash = hash(g);
      final Long prevCycle = cache.get(hash);
      if (prevCycle != null) {
        final long diff = cycle - prevCycle;
        final long numFullSteps = (MAXCYCLES - cycle) / diff;
        cycle += diff * numFullSteps;
      } else {
        cache.put(hash, cycle);
      }
      tilt(g, Vec2.NORTH);
      tilt(g, Vec2.WEST);
      tilt(g, Vec2.SOUTH);
      tilt(g, Vec2.EAST);
      cycle++;
    }
  }

  private long hash(Grid<Character> grid) {
    return grid.stream().mapToLong(e -> (long) e.getValue()).reduce(0L, (a, b) -> a * 33 + b);
  }

  private static void tilt(Grid<Character> g, Vec2 dir) {
    g.forEach((row, col, value) -> {
      if (g.get(row, col) == 'O') {
        tilt(g, Vec2.grid(row, col), dir);
      }
    });
  }

  private static void tilt(Grid<Character> g, Vec2 pos, Vec2 dir) {
    while (true) {
      Vec2 next = pos.add(dir);
      if (g.get(next) == 'O') {
        tilt(g, next, dir);
      }
      if (g.get(next) != '.') {
        return;
      }
      g.swap(pos, next);
      pos = next;
    }
  }
}
