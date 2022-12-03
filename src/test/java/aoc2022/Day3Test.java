package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day3Test {

  private Day day = new Day3("2022/day3.in");

  @Test
  public void testPart1() {
    assertEquals(8039, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(2510, day.solvePart2());
  }
}