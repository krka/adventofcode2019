package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day12Test {

  private Day day = new Day12("2022/day12.in");
  private Day sample = new Day12("2022/day12-sample.in");

  @Test
  public void testPart1() {
    assertEquals(497, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(31, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(492, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(29, sample.solvePart2());
  }
}