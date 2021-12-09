package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day9Test {
  @Test
  public void testPart1() {
    assertEquals(486, new Day9("2021/day9.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(1059300, new Day9("2021/day9.in").solvePart2());
  }

}