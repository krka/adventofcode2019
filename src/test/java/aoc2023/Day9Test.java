package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day9Test {

  private static Day day = new Day9("2023/day9.in");
  private static Day sample = new Day9("2023/day9-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(114, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(2, sample.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(1939607039, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(1041, day.solvePart2());
  }
}