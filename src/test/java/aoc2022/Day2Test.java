package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day2Test {

  private Day day = new Day2("2022/day2.in");

  @Test
  public void testPart1() {
    assertEquals(12855, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(13726, day.solvePart2());
  }
}