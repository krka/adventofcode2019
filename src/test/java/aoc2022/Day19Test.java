package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Day19Test {

  private Day day = new Day19("2022/day19.in");
  private Day sample = new Day19("2022/day19-sample.in");

  @Test
  public void testPart1() {
    final long actual = day.solvePart1();
    assertNotEquals(1008, actual);
    assertNotEquals(1073, actual);
    assertEquals(1127, actual);
  }

  @Test
  public void testPart1Sample() {
    assertEquals(33, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(21546, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(56 * 62, sample.solvePart2());
  }
}