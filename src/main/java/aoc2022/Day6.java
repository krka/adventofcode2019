package aoc2022;

import util.Day;
import util.DayS;
import util.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Day6 implements Day {

  private final List<List<String>> input;

  public Day6(String name) {
    input = Util.readResource(name).stream().collect(Util.splitBy(s -> s.isEmpty()));
  }

  @Override
  public long solvePart1() {
    final String s = input.get(0).get(0);
    return solve(s, 4);
  }

  private long solve(String s, int wanted) {
    for (int i = wanted - 1; i < s.length(); i++) {
      Set<Character> set = new HashSet<>();
      for (int j = 0; j < wanted; j++) {
        set.add(s.charAt(i - j));
      }
      if (set.size() == wanted) {
        return i + 1;
      }
    }
    throw new RuntimeException();
  }

  @Override
  public long solvePart2() {
    final String s = input.get(0).get(0);
    return solve(s, 14);
  }

}
