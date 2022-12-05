package aoc2022;

import org.junit.Test;
import util.Day;
import util.DayS;

import static org.junit.Assert.assertEquals;

public class Day5Test {

  private DayS day = new Day5("2022/day5.in");

  @Test
  public void testPart1() {
    assertEquals("DHBJQJCCW", day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals("WJVRLSJJT", day.solvePart2());
  }
}