package aoc2021;

import util.Grid;
import util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day25 implements Day {


  private final Grid<Character> input;

  public Day25(List<String> input) {
    this.input = Grid.from(input.stream()
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList()), '.', c -> c);
  }

  public static Day25 fromResource(String name) {
    return new Day25(Util.readResource(name));
  }


  @Override
  public long solvePart1() {
    final int rows = input.rows();
    final int cols = input.cols();
    final AtomicBoolean moved = new AtomicBoolean(false);
    for (int step = 1; true; step++) {
      moved.set(false);
      input.forEach((row, col, value) -> {
        final int col2 = (col + 1) % cols;
        if (value == '>' && input.get(row, col2) == '.') {
          input.set(row, col, '1');
          moved.set(true);
        }
      });
      input.forEach((row, col, value) -> {
        final int col2 = (col + 1) % cols;
        if (value == '1') {
          input.set(row, col, '.');
          input.set(row, col2, '>');
        }
      });
      input.forEach((row, col, value) -> {
        final int row2 = (row + 1) % rows;
        if (value == 'v' && input.get(row2, col) == '.') {
          input.set(row, col, '1');
          moved.set(true);
        }
      });
      input.forEach((row, col, value) -> {
        final int row2 = (row + 1) % rows;
        if (value == '1') {
          input.set(row, col, '.');
          input.set(row2, col, 'v');
        }
      });
      if (!moved.get()) {
        return step;
      }
    }
  }

  @Override
  public long solvePart2() {
    return 0;
  }

}