package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day6Test {

  private static Day day = new Day6("2023/day6.in");
  private static Day sample = new Day6("2023/day6-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(288, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(71503, sample.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(800280, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(45128024, day.solvePart2());
  }
}