package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day20Test {

  private static Day day = Day20.fromResource("2021/day20.in");
  private static Day sample = Day20.fromResource("2021/day20-sample.in");

  @Test
  public void testPart1() {
    assertEquals(5301, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(19492, day.solvePart2());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(35, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(3351, sample.solvePart2());
  }

}