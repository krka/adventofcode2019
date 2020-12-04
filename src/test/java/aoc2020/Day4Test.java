package aoc2020;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day4Test {
  @Test
  public void testPart1() {
    assertEquals(260, new Day4("2020/day4.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(153, new Day4("2020/day4.in").solvePart2());
  }

}