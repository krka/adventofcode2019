package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day14Test {

  private Day day = new Day14("2022/day14.in");
  private Day sample = new Day14("2022/day14-sample.in");

  @Test
  public void testPart1() {
    assertEquals(961, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(24, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(26375, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(93, sample.solvePart2());
  }
}