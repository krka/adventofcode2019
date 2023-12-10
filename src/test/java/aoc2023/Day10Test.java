package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day10Test {

  private static Day day = new Day10("2023/day10.in");

  @Test
  public void testPart1() {
    assertEquals(6820, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(337, day.solvePart2());
  }
}