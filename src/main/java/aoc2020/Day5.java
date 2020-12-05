package aoc2020;

import util.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 {
  private final List<String> input;

  public Day5(String name) {
    input = Util.readResource(name);
  }

  public long solvePart1() {
    return input.stream()
            .map(Day5::repl)
            .mapToInt(s -> Integer.parseInt(s, 2))
            .max().getAsInt();
  }

  public long solvePart2() {

    List<Integer> list = input.stream()
            .map(Day5::repl)
            .map(s -> Integer.parseInt(s, 2))
            .sorted()
            .collect(Collectors.toList());
    for (int i = 0; i < list.size() - 1; i++) {
      Integer a = list.get(i);
      Integer b = list.get(i + 1);
      if (a + 2 == b) {
        return a + 1;
      }
    }
    throw new RuntimeException();
  }

  static String repl(String s) {
    return s.replaceAll("[FL]", "0").replaceAll("[BR]", "1");
  }
}
