package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day18Test {

  private Day day = Day18.fromResource("2021/day18.in");

  @Test
  public void testPart1() {
    assertEquals(3892, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(4909, day.solvePart2());
  }


}