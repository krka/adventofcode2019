package aoc2020;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day8Test {
  @Test
  public void testPart1() {
    assertEquals(1939, new Day8("2020/day8.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(2212, new Day8("2020/day8.in").solvePart2());
  }

}