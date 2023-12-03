package aoc2023;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Character.isDigit;

public class Day3 implements Day {

  private final Grid<Character> grid;

  public Day3(String name) {
    grid = Grid.from(Util.readResource(name), '.', Function.identity());
  }

  @Override
  public long solvePart1() {
    AtomicLong sum = new AtomicLong();
    grid.forEach((row, col, value) -> {
      if (isDigit(value) && !isDigit(grid.get(row, col - 1))) {
        final String digits = digits(row, col);
        AtomicBoolean isPart = new AtomicBoolean(false);
        for (int i = 0; i < digits.length(); i++) {
          int finalI = i;
          Vec2.DIRS_8.forEach(vec2 -> {
            final long r = vec2.row() + row;
            final long c = vec2.col() + col + finalI;
            final Character ch = grid.get(r, c);
            if (isPart(ch)) {
              isPart.set(true);
            }
          });
        }
        if (isPart.get()) {
          sum.addAndGet(Integer.parseInt(digits));
        }
      }
    });
    return sum.get();
  }

  private static boolean isPart(char c) {
    return c != '.' && !isDigit(c);
  }

  private String digits(int row, int col) {
    StringBuilder sb = new StringBuilder();
    while (true) {
      final Character c = grid.get(row, col);
      if (isDigit(c)) {
        sb.append(c);
        col++;
      } else {
        return sb.toString();
      }
    }
  }

  @Override
  public long solvePart2() {
    AtomicLong sum = new AtomicLong();
    grid.forEach((row, col, value) -> {
      if (value == '*') {
        List<String> parts = new ArrayList<>();
        if (isDigit(grid.get(row, col - 1))) {
          parts.add(findPart(row, col - 1));
        }
        if (isDigit(grid.get(row, col + 1))) {
          parts.add(findPart(row, col + 1));
        }
        if (isDigit(grid.get(row - 1, col))) {
          parts.add(findPart(row - 1, col));
        } else {
          if (isDigit(grid.get(row - 1, col - 1))) {
            parts.add(findPart(row - 1, col - 1));
          }
          if (isDigit(grid.get(row - 1, col + 1))) {
            parts.add(findPart(row - 1, col + 1));
          }
        }
        if (isDigit(grid.get(row + 1, col))) {
          parts.add(findPart(row + 1, col));
        } else {
          if (isDigit(grid.get(row + 1, col - 1))) {
            parts.add(findPart(row + 1, col - 1));
          }
          if (isDigit(grid.get(row + 1, col + 1))) {
            parts.add(findPart(row + 1, col + 1));
          }
        }
        final List<Long> gears = parts.stream().filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
        if (gears.size() == 2) {
          sum.addAndGet(gears.get(0) * gears.get(1));
        }
      }
    });
    return sum.get();
  }

  private String findPart(int row, int col) {
    final StringBuilder sb = new StringBuilder();
    while (isDigit(grid.get(row, col - 1))) {
      col--;
    }
    while (isDigit(grid.get(row, col))) {
      sb.append(grid.get(row, col));
      col++;
    }
    return sb.toString();
  }

}
