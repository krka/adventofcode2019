package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day15Test {

  private static Day day = new Day15("2023/day15.in");
  private static Day sample = new Day15("2023/day15-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(1320, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(145, sample.solvePart2());
  }


  @Test
  public void testPart1() {
    assertEquals(512950, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(247153, day.solvePart2());
  }
}