package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day14Test {

  private static Day day = new Day14("2023/day14.in");
  private static Day sample = new Day14("2023/day14-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(136, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(64, sample.solvePart2());
  }


  @Test
  public void testPart1() {
    assertEquals(110565, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(89845, day.solvePart2());
  }
}