package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day4Test {

  private Day day = new Day4("2022/day4.in");

  @Test
  public void testPart1() {
    assertEquals(475, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(825, day.solvePart2());
  }
}