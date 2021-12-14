package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day14Test {

  private Day day = new Day14("2021/day14.in");

  @Test
  public void testPart1() {
    assertEquals(2345, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(2432786807053L, day.solvePart2());
  }

}