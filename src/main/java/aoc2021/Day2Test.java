package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day2Test {
  @Test
  public void testPart1() {
    assertEquals(2117664, new Day2("2021/day2.in").solvePart1());
  }

  @Test
  public void testPart2() {
    // Not 2117664
    assertEquals(2073416724, new Day2("2021/day2.in").solvePart2());
  }

}