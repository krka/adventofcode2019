package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day1Test {
  @Test
  public void testPart1() {
    assertEquals(1616, new Day1("2021/day1.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(1645, new Day1("2021/day1.in").solvePart2());
  }

}