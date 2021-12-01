package aoc2021;

import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

  private final List<String> input;

  public Day1(String name) {
    input = Util.readResource(name);
  }

  public long solvePart1() {
    return input.stream()
            .map(Integer::parseInt)
            .collect(Util.adj()).stream()
            .filter(pair -> pair.b() > pair.a())
            .count();
  }

  public long solvePart2() {
    List<Integer> integers = input.stream()
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    List<Integer> sums = new ArrayList<>();
    for (int i = 0; i < integers.size() - 2; i++) {
      sums.add(integers.get(i) + integers.get(i + 1) + integers.get(i + 2));
    }
    return sums.stream().collect(Util.adj()).stream()
            .filter(pair -> pair.b() > pair.a())
            .count();
  }
}
