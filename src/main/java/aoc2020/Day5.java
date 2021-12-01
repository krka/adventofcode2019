package aoc2020;

import util.Util;

import java.util.List;

public class Day5 {
  private final List<String> input;

  public Day5(String name) {
    input = Util.readResource(name);
  }

  public long solvePart1() {
    return input.stream()
            .map(Day5::repl)
            .mapToInt(s -> Integer.parseInt(s, 2))
            .max()
            .getAsInt();
  }

  public long solvePart2() {
    return input.stream()
            .map(Day5::repl)
            .map(s -> Integer.parseInt(s, 2))
            .sorted()
            .collect(Util.windows(2)).stream()
            .filter(pair -> pair.get(0) + 2 == pair.get(1))
            .mapToInt(pair -> pair.get(0) + 1)
            .reduce(Util.exactlyOneInt())
            .getAsInt();
  }

  static String repl(String s) {
    return s.replaceAll("[FL]", "0").replaceAll("[BR]", "1");
  }
}
