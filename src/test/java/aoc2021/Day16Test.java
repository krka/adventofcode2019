package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day16Test {

  private Day day = Day16.fromResource("2021/day16.in");

  @Test
  public void testPart1() {
    assertEquals(860, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(470949537659L, day.solvePart2());
  }

}