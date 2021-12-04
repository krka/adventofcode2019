package aoc2021;

import util.Util;

import java.util.List;

public class Day1 {

  private final List<String> input;

  public Day1(String name) {
    input = Util.readResource(name);
  }

  public long solvePart1() {
    return input.stream()
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .collect(Util.windows(2)).stream()
            .filter(pair -> pair.get(1) > pair.get(0))
            .count();
  }

  public long solvePart2() {
    return input.stream()
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .collect(Util.windows(3)).stream()
            .map(tuple -> tuple.stream().mapToInt(Integer::intValue).sum())
            .collect(Util.windows(2)).stream()
            .filter(pair -> pair.get(1) > pair.get(0))
            .count();
  }
}
