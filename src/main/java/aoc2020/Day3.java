package aoc2020;

import util.Util;

import java.util.List;

public class Day3 {

  private final List<String> input;

  public Day3(String name) {
    input = Util.readResource(name);
  }

  public int solvePart1() {
    return traverse(3, 1);
  }

  public long solvePart2() {
    long mul = 1;
    mul *= traverse(1, 1);
    mul *= traverse(3, 1);
    mul *= traverse(5, 1);
    mul *= traverse(7, 1);
    mul *= traverse(1, 2);
    return mul;
  }

  private int traverse(int colDelta, int rowDelta) {
    int count = 0;
    for (int row = 0; row < input.size(); row += rowDelta) {
      String line = input.get(row);
      int col2 = colDelta * row % line.length();
      if (line.charAt(col2) == '#') {
        count++;
      }
    }
    return count;
  }
}
