package aoc2023;

import util.Day;
import util.Util;

import java.util.List;

public class Day18 implements Day {
  private final List<List<String>> lines;

  public Day18(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
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

