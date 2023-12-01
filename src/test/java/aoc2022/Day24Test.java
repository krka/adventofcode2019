package aoc2022;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day24Test {

  private Day24 day = new Day24("2022/day24.in");
  private Day24 sample = new Day24("2022/day24-sample.in");

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

  @Test
  public void testPart3() {
    final long actual = day.solveForTarget(10_000L);
    assertEquals(6000334, actual);
  }

  @Test
  public void testPart3Sample() {
    assertEquals(360018, sample.solveForTarget(10_000L));
  }

  @Test
  public void testPart4() {
    final long actual = day.solveForTarget(1_000_000_000);
    assertEquals(600000000334L, actual);
  }

  @Test
  public void testPart4Sample() {
    assertEquals(36000000018L, sample.solveForTarget(1_000_000_000));
  }
}