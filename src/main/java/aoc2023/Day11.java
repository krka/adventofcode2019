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
    long[] rowWeights = Util.newLongArray(grid.rows(), weight);
    long[] colWeights = Util.newLongArray(grid.cols(), weight);
    List<Vec2> galaxies = new ArrayList<>();
    grid.forEach((row, col, value) -> {
      if (value == '#') {
        rowWeights[row] = colWeights[col] = 1;
        galaxies.add(Vec2.grid(row, col));
      }
    });

    long[] rowCoords = Util.accumulate(rowWeights);
    long[] colCoords = Util.accumulate(colWeights);

    List<Vec2> translated = galaxies.stream()
            .map(g -> Vec2.grid(rowCoords[g.irow()], colCoords[g.icol()]))
            .collect(Collectors.toList());

    return Util.distinctPairs(translated).stream()
            .mapToLong(p -> p.a().sub(p.b()).manhattan())
            .sum();
  }
}
