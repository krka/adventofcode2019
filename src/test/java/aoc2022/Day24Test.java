package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day24Test {

  private Day day = new Day24("2022/day24.in");
  private Day sample = new Day24("2022/day24-sample.in");

  @Test
  public void testPart1() {
    final long actual = day.solvePart1();
    assertEquals(334, actual);
  }

  @Test
  public void testPart1Sample() {
    assertEquals(18, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    final long actual = day.solvePart2();
    assertEquals(934, actual);
  }

  @Test
  public void testPart2Sample() {
    assertEquals(54, sample.solvePart2());
  }
}