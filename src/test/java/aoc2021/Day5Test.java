package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day5Test {
  @Test
  public void testPart1() {
    assertEquals(6397, new Day5("2021/day5.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(22335, new Day5("2021/day5.in").solvePart2());
  }

}