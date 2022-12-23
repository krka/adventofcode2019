package aoc2022;

import util.Day;
import util.Grid;
import util.Util;

import java.util.List;

public class Day10 implements Day {

  private final List<String> input;
  private final int[] strengths;

  public Day10(String name) {
    input = Util.readResource(name);
    int cycle = 0;
    int x = 1;
    strengths = new int[240];
    for (String s : input) {
      strengths[cycle] = x;
      final String[] parts = s.split(" ");
      final String instr = parts[0];
      if (instr.equals("noop")) {
        cycle += 1;
      } else if (instr.equals("addx")) {
        strengths[cycle + 1] = x;
        x += Long.parseLong(parts[1]);
        cycle += 2;
      }
    }

  }

  @Override
  public long solvePart1() {
    int sum = 0;
    for (int i = 0; i < 6; i++) {
      int cycleIndex = 20 + i * 40;
      sum += cycleIndex * strengths[cycleIndex - 1];
    }
    return sum;
  }

  @Override
  public long solvePart2() {
    final Grid<Character> grid = Grid.create(6, 40, ' ');
    for (int i = 0; i < 240; i++) {
      int col = i % 40;
      int row = i / 40;
      final long x2 = strengths[i];
      grid.set(row, col, Math.abs(x2 - col) <= 1 ? '#' : ' ');
    }
    System.out.println(grid.toString());
    return 0;
  }
}


