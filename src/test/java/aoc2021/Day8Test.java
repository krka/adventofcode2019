package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day8Test {
  @Test
  public void testPart1() {
    assertEquals(390, new Day8("2021/day8.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(1011785, new Day8("2021/day8.in").solvePart2());
  }

}