package aoc2021;

import util.Grid;
import util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class Day11 implements Day {

  private final List<String> input;
  private final Grid<Integer> grid;
  private int numFlashes = 0;

  public Day11(String name) {
    this.input = Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    grid = Grid.from(input, 0, c -> c - '0');
  }

  public long solvePart1() {
    for (int i = 0; i < 100; i++) {
      step();
    }
    return numFlashes;
  }

  private void step() {
    grid.forEach(this::inc);
    grid.forEach((row, col, value) -> {
      if (value > 9) {
        grid.set(row, col, 0);
      }
    });
  }

  private void inc(int row, int col, int cur) {
    grid.set(row, col, cur + 1);
    if (cur == 9) {
      numFlashes++;
      grid.forEachNeighbour(row, col, this::inc);
    }
  }


  public long solvePart2() {
    for (int i = 1; true; i++) {
      int prevFlashes = numFlashes;
      step();
      if (numFlashes - prevFlashes == 100) {
        return i;
      }
    }
  }


}
