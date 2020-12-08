package aoc2018;

import org.junit.Test;
import util.Util;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Day2Test {
  final static List<String> input = Util.readResource(("2018/day2.in"));

  @Test
  public void testPart1() {
    assertEquals(5880, solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals("tiwcdpbseqhxryfmgkvjujvza", solvePart2());
  }

  private int solvePart1() {
    int twos = 0;
    int threes = 0;
    for (String s : input) {
      twos += s.chars().sorted().boxed().collect(Util.toPartitions(Objects::equals)).stream()
              .mapToInt(List::size)
              .filter(value -> value == 2)
              .map(value -> 1)
              .findFirst().orElseGet(() -> 0);
      threes += s.chars().sorted().boxed().collect(Util.toPartitions(Objects::equals)).stream()
              .mapToInt(List::size)
              .filter(value -> value == 3)
              .map(value -> 1)
              .findFirst().orElseGet(() -> 0);
    }
    return twos * threes;
  }

  private String solvePart2() {
    int max = input.stream().mapToInt(String::length).max().getAsInt();
    for (int i = 0; i < max; i++) {
      String common = find(i);
      if (common != null) {
        return common;
      }
    }
    throw new RuntimeException();
  }

  private String find(int skip) {
    return input.stream().map(s -> s.substring(0, skip) + s.substring(skip + 1))
            .collect(Collectors.toMap(s -> s, x -> 1, Integer::sum))
            .entrySet().stream()
            .filter(e -> e.getValue() == 2)
            .reduce(Util.exactlyOne())
            .map(Map.Entry::getKey)
            .orElse(null);
  }

}