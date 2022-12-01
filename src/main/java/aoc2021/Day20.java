package aoc2021;

import util.Day;
import util.Grid;
import util.Util;

import java.util.List;

public class Day20 implements Day {

  private final String algo;
  private final Grid<Character> grid;
  private final char allDark;
  private final char allLight;

  public Day20(List<String> input) {
    this.algo = input.get(0);
    allDark = algo.charAt(0);
    allLight = algo.charAt(511);
    grid = Grid.from(input.subList(2, input.size() - 1), '.', c -> c);
  }

  public static Day20 fromResource(String name) {
    return new Day20(Util.readResource(name));
  }

  public long solvePart1() {
    return enhanceTimes(2).count(character -> character == '#');
  }

  public long solvePart2() {
    return enhanceTimes(50).count(character -> character == '#');
  }

  private Grid<Character> enhanceTimes(int times) {
    Grid<Character> x = grid;
    for (int i = 0; i < times; i++) {
      x = enhance(x);
    }
    return x;
  }

  private Grid<Character> enhance(Grid<Character> grid) {
    char defaultValue = grid.getDefaultValue() == '#' ? allLight : allDark;
    Grid<Character> newGrid = new Grid<>(grid.rows() + 2, grid.cols() + 2, defaultValue);
    newGrid.forEach((row, col, value) -> {
      int index = 0;
      for (int r = -2; r < 1; r++) {
        for (int c = -2; c < 1; c++) {
          index = index * 2 + (grid.get(row + r, col + c) == '#' ? 1 : 0);
        }
      }
      newGrid.set(row, col, algo.charAt(index));
    });
    return newGrid;
  }
}
