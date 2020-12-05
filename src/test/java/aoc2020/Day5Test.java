package aoc2020;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day5Test {
  @Test
  public void testPart1() {
    assertEquals(951, new Day5("2020/day5.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(653, new Day5("2020/day5.in").solvePart2());
  }

}