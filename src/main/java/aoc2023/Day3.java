package aoc2023;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static java.lang.Character.isDigit;

public class Day3 implements Day {

  private final Grid<Character> grid;
  private final Grid<AtomicInteger> numbers;

  public Day3(String name) {
    grid = Grid.from(Util.readResource(name), '.', Function.identity());
    numbers = Grid.create(grid.rows(), grid.cols(), null);
    grid.forEach((row, col, value) -> {
      if (isDigit(value) && !isDigit(grid.get(row, col - 1))) {
        StringBuilder sb = new StringBuilder();
        int c = col;
        while (isDigit(value)) {
          sb.append(value);
          value = grid.get(row, ++c);
        }
        AtomicInteger part = new AtomicInteger(Integer.parseInt(sb.toString()));
        for (int i = col; i < c; i++) {
          numbers.set(row, i, part);
        }
      }
    });
  }

  @Override
  public long solvePart1() {
    Set<AtomicInteger> validParts = new HashSet<>();
    grid.forEach((row, col, value) -> {
      if (!isDigit(value) && value != '.') {
        Vec2.DIRS_8.forEach(dir -> {
          final AtomicInteger maybePart = numbers.get(row + dir.row(), col + dir.col());
          if (maybePart != null) {
            validParts.add(maybePart);
          }
        });
      }
    });
    return validParts.stream().mapToLong(AtomicInteger::longValue).sum();
  }

  @Override
  public long solvePart2() {
    AtomicLong sum = new AtomicLong();
    grid.forEach((row, col, value) -> {
      if (value == '*') {
        final long[] parts = Vec2.DIRS_8.stream()
                .map(dir -> numbers.get(row + dir.row(), col + dir.col()))
                .filter(Objects::nonNull)
                .distinct()
                .mapToLong(AtomicInteger::longValue)
                .toArray();
        if (parts.length == 2) {
          sum.addAndGet(parts[0] * parts[1]);
        }
      }
    });
    return sum.get();
  }
}
