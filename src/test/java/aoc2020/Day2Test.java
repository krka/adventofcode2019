package aoc2020;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day2Test {
  @Test
  public void testPart1() {
    assertEquals(542, new Day2("2020/day2.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(360, new Day2("2020/day2.in").solvePart2());
  }


}