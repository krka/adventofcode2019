package aoc2023;

import util.Day;
import util.Grid;
import util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class Day13 implements Day {

  private static final int FLIP = '#' + '.';

  private final List<List<String>> lines;
  private final List<Grid<Character>> grids;

  public Day13(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    grids = lines.stream().map(strings -> Grid.from(strings, 'X', x -> x)).collect(Collectors.toList());
  }

  @Override
  public long solvePart1() {
    return grids.stream().mapToLong(g -> solve(g, -1)).sum();
  }

  @Override
  public long solvePart2() {
    return grids.stream().mapToLong(g -> part2(g, solve(g, -1))).sum();
  }

  private long solve(Grid<Character> grid, long avoid) {
    return Math.max(solveRow(grid, avoid), solveCol(grid, avoid));
  }

  private static long solveCol(Grid<Character> grid, long avoid) {
    for (int c = 0; c < grid.cols() - 1; c++) {
      if (matchCols(grid, c, c + 1)) {
        final long res = c + 1;
        if (res != avoid) {
          return res;
        }
      }
    }
    return -1;
  }

  private static long solveRow(Grid<Character> grid, long avoid) {
    for (int r = 0; r < grid.rows() - 1; r++) {
      if (matchRows(grid, r, r + 1)) {
        final long res = 100L * (r + 1);
        if (res != avoid) {
          return res;
        }
      }
    }
    return -1;
  }

  private static boolean matchRows(Grid<Character> grid, int r1, int r2) {
    if (r1 < 0 || r2 >= grid.rows()) {
      return true;
    }
    for (int c = 0; c < grid.cols(); c++) {
      final char c1 = grid.get(r1, c);
      final char c2 = grid.get(r2, c);
      if (c1 != c2) {
        return false;
      }
    }
    return matchRows(grid, r1 - 1, r2 + 1);
  }

  private static boolean matchCols(Grid<Character> grid, int c1, int c2) {
    if (c1 < 0 || c2 >= grid.cols()) {
      return true;
    }
    for (int r = 0; r < grid.rows(); r++) {
      final char chh1 = grid.get(r, c1);
      final char chh2 = grid.get(r, c2);
      if (chh1 != chh2) {
        return false;
      }
    }
    return matchCols(grid, c1 - 1, c2 + 1);
  }

  private long part2(Grid<Character> grid, long avoid) {
    for (int r = 0; r < grid.rows(); r++) {
      for (int c = 0; c < grid.cols(); c++) {
        final char prev = grid.get(r, c);
        grid.set(r, c, (char) (FLIP - prev));
        final long res = solve(grid, avoid);
        grid.set(r, c, prev);
        if (res > 0) {
          return res;
        }
      }
    }
    throw new RuntimeException();
  }
}
