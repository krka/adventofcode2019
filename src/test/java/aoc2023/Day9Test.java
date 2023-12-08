package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day9Test {

  private static Day day = new Day9("2023/day9.in");
  private static Day sample = new Day9("2023/day9-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(0, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(0, sample.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(0, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(0, day.solvePart2());
  }
}