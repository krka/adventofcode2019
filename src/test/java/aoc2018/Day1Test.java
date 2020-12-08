package aoc2018;

import org.junit.Test;
import util.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class Day1Test {
  final static List<String> input = Util.readResource(("2018/day1.in"));

  @Test
  public void testPart1() {
    assertEquals(408, solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(55250, solvePart2());
  }

  private int solvePart1() {
    return input.stream().mapToInt(Integer::parseInt).sum();
  }

  private int solvePart2() {
    Set<Integer> visited = new HashSet<>();
    int freq = 0;
    for (int i = 0; true; i++) {
      freq += Integer.parseInt(input.get(i % input.size()));
      if (!visited.add(freq)) {
        return freq;
      }
    }
  }

}