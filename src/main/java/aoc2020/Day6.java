package aoc2020;

import util.Util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6 {
  private final List<String> input;

  public Day6(String name) {
    input = Util.readResource(name);
  }

  public long solvePart1() {
    return input.stream()
            .collect(Util.splitBy(String::isEmpty)).stream()
            .mapToInt(g -> g.stream()
                    .map(String::chars).map(IntStream::boxed)
                    .map(s -> s.collect(Collectors.toSet()))
                    .reduce(Util::union)
                    .map(Set::size).orElse(0))
            .sum();
  }

  public long solvePart2() {
    return input.stream()
            .collect(Util.splitBy(String::isEmpty)).stream()
            .mapToInt(g -> g.stream()
                    .map(String::chars).map(IntStream::boxed)
                    .map(s -> s.collect(Collectors.toSet()))
                    .reduce(Util::intersection)
                    .map(Set::size).orElse(0))
            .sum();
  }


}
