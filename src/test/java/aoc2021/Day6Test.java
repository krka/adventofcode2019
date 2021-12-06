package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day6Test {
  @Test
  public void testPart1() {
    assertEquals(352195, new Day6("2021/day6.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(1600306001288L, new Day6("2021/day6.in").solvePart2());
  }

}