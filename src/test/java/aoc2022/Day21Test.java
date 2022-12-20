package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day21Test {

  private Day day = new Day21("2022/day21.in");
  private Day sample = new Day21("2022/day21-sample.in");

  @Test
  public void testPart1() {
    final long actual = day.solvePart1();
    assertEquals(1, actual);
  }

  @Test
  public void testPart1Sample() {
    assertEquals(1, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(1, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(1, sample.solvePart2());
  }
}