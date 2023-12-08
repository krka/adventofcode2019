package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day8Test {

  private static Day day = new Day8("2023/day8.in");
  private static Day sample = new Day8("2023/day8-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(2, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(2, sample.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(19637, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(8811050362409L, day.solvePart2());
  }
}