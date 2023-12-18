package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day18Test {

  private static Day day = new Day18("2023/day18.in");
  private static Day sample = new Day18("2023/day18-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(62, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(952408144115L, sample.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(45159, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(134549294799713L, day.solvePart2());
  }
}