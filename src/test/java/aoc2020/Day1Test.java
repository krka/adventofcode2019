package aoc2020;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day1Test {
  @Test
  public void testPart1() {
    assertEquals(270144, new Day1("2020/day1.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(261342720, new Day1("2020/day1.in").solvePart2());
  }


}