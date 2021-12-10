package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day10Test {
  @Test
  public void testPart1() {
    assertEquals(215229, new Day10("2021/day10.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(1105996483, new Day10("2021/day10.in").solvePart2());
  }

}