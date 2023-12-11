package aoc2023;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.Arrays;
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
    long[] rowWeights = new long[grid.rows()];
    long[] colWeights = new long[grid.cols()];
    Arrays.fill(rowWeights, weight);
    Arrays.fill(colWeights, weight);
    List<Vec2> galaxies = new ArrayList<>();
    grid.forEach((row, col, value) -> {
      if (value == '#') {
        rowWeights[row] = colWeights[col] = 1;
        galaxies.add(Vec2.grid(row, col));
      }
    });

    long[] rowCoords = Util.accumulate(rowWeights);
    long[] colCoords = Util.accumulate(colWeights);

    List<Vec2> galaxies2 = galaxies.stream()
            .map(galaxy -> Vec2.grid(rowCoords[galaxy.irow()], colCoords[galaxy.icol()]))
            .collect(Collectors.toList());

    return galaxies2.stream().mapToLong(g1 ->
            galaxies2.stream().mapToLong(g2 -> g1.sub(g2).manhattan()).sum())
            .sum() / 2;
  }
}