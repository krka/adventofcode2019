package aoc2022;

import org.junit.Test;
import util.Day;
import util.DayS;

import static org.junit.Assert.assertEquals;

public class Day6Test {

  private Day day = new Day6("2022/day6.in");

  @Test
  public void testPart1() {
    assertEquals(1542, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(3153, day.solvePart2());
  }
}