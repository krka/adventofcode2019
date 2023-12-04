package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day4Test {

  private static Day day = new Day4("2023/day4.in");
  private static Day sample = new Day4("2023/day4-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(13, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(30, sample.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(22193, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(5625994, day.solvePart2());
  }
}