package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day3Test {
  @Test
  public void testPart1() {
    assertEquals(1540244, new Day3("2021/day3.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(4203981, new Day3("2021/day3.in").solvePart2());
  }

}