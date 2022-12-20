package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Day20Test {

  private Day day = new Day20("2022/day20.in");
  private Day sample = new Day20("2022/day20-sample.in");

  @Test
  public void testPart1() {
    final long actual = day.solvePart1();
    assertEquals(2622, actual);
  }

  @Test
  public void testPart1Sample() {
    assertEquals(3, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(1538773034088L, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(1623178306L, sample.solvePart2());
  }
}