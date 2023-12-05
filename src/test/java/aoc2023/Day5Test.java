package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day5Test {

  private static Day day = new Day5("2023/day5.in");
  private static Day sample = new Day5("2023/day5-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(35, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(46, sample.solvePart2());
  }

  @Test
  public void testPart1() {
    // not 8667739
    assertEquals(486613012L, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(56931769, day.solvePart2());
  }
}