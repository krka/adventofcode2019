package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day7Test {
  @Test
  public void testPart1() {
    assertEquals(356958, new Day7("2021/day7.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(105461913, new Day7("2021/day7.in").solvePart2());
  }

}