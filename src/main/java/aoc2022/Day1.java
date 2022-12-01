package aoc2022;

import util.Day;
import util.Util;

import java.util.List;

public class Day1 implements Day {

  private final List<List<String>> parts;

  public Day1(String name) {
    parts = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
  }

  @Override
  public long solvePart1() {
    return parts.stream()
            .mapToLong(strings -> strings.stream().mapToLong(Long::parseLong).sum())
            .max().getAsLong();
  }

  @Override
  public long solvePart2() {
    return -parts.stream().mapToLong(strings -> strings.stream().mapToLong(Long::parseLong).sum())
            .map(x -> -x)
            .sorted().limit(3).sum();
  }
}
