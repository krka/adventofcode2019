package aoc2023;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 implements Day {

  private final List<List<String>> lines;
  private final Grid<Character> grid;

  public Day11(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    grid = Grid.from(lines.get(0), '.', x -> x);
  }

  @Override
  public long solvePart1() {
    return solve(2);
  }

  @Override
  public long solvePart2() {
    return solve(1000000);
  }

  private long solve(int weight) {
    int[] rowcounts = new int[grid.rows()];
    int[] colcounts = new int[grid.cols()];
    List<Vec2> galaxies = new ArrayList<>();
    grid.forEach((row, col, value) -> {
      if (value == '#') {
        rowcounts[row]++;
        colcounts[col]++;
        galaxies.add(Vec2.grid(row, col));
      }
    });

    int[] rowsums = new int[grid.rows()];
    int[] colsums = new int[grid.cols()];
    for (int i = 0; i < grid.rows(); i++) {
      final int prev = i > 0 ? rowsums[i - 1] : 0;
      final int cur = rowcounts[i] == 0 ? weight : 1;
      rowsums[i] = prev + cur;
    }
    for (int i = 0; i < grid.cols(); i++) {
      final int prev = i > 0 ? colsums[i - 1] : 0;
      final int cur = colcounts[i] == 0 ? weight : 1;
      colsums[i] = prev + cur;
    }

    List<Vec2> galaxies2 = galaxies.stream()
            .map(galaxy -> Vec2.grid(colsums[(int) galaxy.col()], rowsums[(int) galaxy.row()]))
            .collect(Collectors.toList());

    return galaxies2.stream().mapToLong(g1 ->
            galaxies2.stream().mapToLong(g2 -> g1.sub(g2).manhattan()).sum())
            .sum() / 2;
  }
}
