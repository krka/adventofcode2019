package aoc2022;

import org.junit.Test;
import util.Day;
import util.Util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Day17Test {

  private Day day = new Day17("2022/day17.in");
  private Day sample = new Day17("2022/day17-sample.in");

  @Test
  public void testPart1() {
    assertEquals(3235, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(3068, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    final long actual = day.solvePart2();
    Util.assertTooHigh(actual, 1595362316453L);
    Util.assertTooLow(actual, 1590697674416L);
    assertEquals(1591860465110L, actual);
  }

  @Test
  public void testPart2Sample() {
    assertEquals(1514285714288L, sample.solvePart2());
  }
}