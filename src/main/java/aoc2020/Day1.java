package aoc2020;

import util.Util;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Stack;
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
    return set.stream()
            .filter(x -> set.contains(2020 - x))
            .mapToInt(x -> x * (2020 - x))
            .findAny()
            .getAsInt();
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

  public int solvePart2Rec() {
    List<Integer> set = input.stream().map(Integer::parseInt).collect(Collectors.toList());
    return rec(set, 0, 3, 2020, 1);
  }

  private int rec(List<Integer> values, int index, int remaining, int target, int product) {
    if (remaining == 0) {
      if (target == 0) {
        return product;
      }
      return -1;
    }
    for (int i = index; i < values.size(); i++) {
      int v = values.get(i);
      int ans = rec(values, i + 1, remaining - 1, target - v, product * v);
      if (ans != -1) {
        return ans;
      }
    }
    return -1;
  }
}
