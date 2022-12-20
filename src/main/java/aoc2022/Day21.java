package aoc2022;

import util.Day;
import util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class Day21 implements Day {

  private final List<String> input;

  public Day21(String name) {
    input = Util.readResource(name).stream().filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
  }


  @Override
  public long solvePart1() {
    return 0;
  }

  @Override
  public long solvePart2() {
    return 0;
  }
}
