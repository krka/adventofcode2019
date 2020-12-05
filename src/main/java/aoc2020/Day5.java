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
            .max()
            .getAsInt();
  }

  public long solvePart2() {
    return input.stream()
            .map(Day5::repl)
            .map(s -> Integer.parseInt(s, 2))
            .sorted()
            .collect(Util.adj()).stream()
            .filter(pair -> pair.a() + 2 == pair.b())
            .mapToInt(pair -> pair.a() + 1)
            .reduce(Util.exactlyOne())
            .getAsInt();
  }

  static String repl(String s) {
    return s.replaceAll("[FL]", "0").replaceAll("[BR]", "1");
  }
}
