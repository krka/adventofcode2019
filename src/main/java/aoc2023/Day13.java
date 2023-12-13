package aoc2023;

import util.Day;
import util.Grid;
import util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class Day13 implements Day {
  private final List<List<String>> lines;
  private final List<Grid<Character>> grids;

  public Day13(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    grids = lines.stream().map(strings -> Grid.from(strings, 'X', x -> x)).collect(Collectors.toList());
  }

  @Override
  public long solvePart1() {
    return grids.stream().mapToLong(g -> solve(g, 0)).sum();
  }

  @Override
  public long solvePart2() {
    return grids.stream().mapToLong(g -> solve(g, 1)).sum();
  }

  private long solve(Grid<Character> grid, long target) {
    return Math.max(solveRow(grid, target), solveCol(grid, target));
  }

  private static long solveCol(Grid<Character> grid, long target) {
    for (int c = 0; c < grid.cols() - 1; c++) {
      if (matchCols(grid, c) == target) {
        return c + 1;
      }
    }
    return -1;
  }

  private static long solveRow(Grid<Character> grid, long target) {
    for (int r = 0; r < grid.rows() - 1; r++) {
      if (matchRows(grid, r) == target) {
        return 100L * (r + 1);
      }
    }
    return -1;
  }

  private static int matchCols(Grid<Character> grid, int c) {
    int diffs = 0;
    int num = Math.min(c + 1, grid.cols() - c - 1);
    for (int i = 0; i < num; i++) {
      for (int r = 0; r < grid.rows(); r++) {
        if (grid.get(r, c - i) != grid.get(r, c + i + 1)) {
          diffs++;
        }
      }
    }
    return diffs;
  }

  private static int matchRows(Grid<Character> grid, int r) {
    int diffs = 0;
    int num = Math.min(r + 1, grid.rows() - r - 1);
    for (int i = 0; i < num; i++) {
      for (int c = 0; c < grid.cols(); c++) {
        if (grid.get(r - i, c) != grid.get(r + i + 1, c)) {
          diffs++;
        }
      }
    }
    return diffs;
  }
}
