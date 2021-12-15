package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day15Test {

  private Day day = new Day15("2021/day15.in");

  @Test
  public void testPart1() {
    assertEquals(363, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(2835, day.solvePart2());
  }

}