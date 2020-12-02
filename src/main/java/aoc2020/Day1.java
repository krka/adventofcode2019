package aoc2020;

import util.Util;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day1 {

  private final List<String> input;

  public Day1(String name) {
    input = Util.readResource(name);
  }

  public int solvePart1() {
    Set<Integer> set = input.stream().map(Integer::parseInt).collect(Collectors.toSet());
    for (Integer x : set) {
      int y = 2020 - x;
      if (set.contains(y)) {
        return x * y;
      }
    }
    throw new RuntimeException();
  }

  public int solvePart2() {
    Set<Integer> set = input.stream().map(Integer::parseInt).collect(Collectors.toSet());
    for (Integer x : set) {
      for (Integer y : set) {
        int z = 2020 - x - y;
        if (set.contains(z)) {
          return x * y * z;
        }
      }
    }
    throw new RuntimeException();
  }
}
