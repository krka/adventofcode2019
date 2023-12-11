package aoc2023;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day12 implements Day {

  private final List<List<String>> lines;
  private final Grid<Character> grid;

  public Day12(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    grid = null;//Grid.from(lines.get(0), '.', x -> x);
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
