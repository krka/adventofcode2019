package aoc2022;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 implements Day {

  private final List<String> input;

  public Day2(String name) {
    input = Util.readResource(name);
  }

  @Override
  public long solvePart1() {
    int sum = 0;
    for (String s : input) {
      final String[] split = s.split(" ");
      final String a = split[0];
      final String b = split[1];
      int first = a.charAt(0) - 'A';
      int second = b.charAt(0) - 'X';

      sum += (second + 1) + 3 * ((4 + second - first) % 3);
    }
    return sum;
  }

  @Override
  public long solvePart2() {
    int sum = 0;
    for (String s : input) {
      final String[] split = s.split(" ");
      final String a = split[0];
      final String b = split[1];
      int first = a.charAt(0) - 'A';
      int second = b.charAt(0) - 'X';
      sum += second * 3 + 1 + (2 + second + first) % 3;
    }
    return sum;
  }
}
