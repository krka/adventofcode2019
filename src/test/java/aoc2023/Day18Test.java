package aoc2023;

import org.junit.Test;
import util.Day;
import util.TestBase;

import static org.junit.Assert.assertEquals;

public class Day18Test {

  private static final Day day = TestBase.create("$YEAR/$DAY.in");
  private static final Day sample = TestBase.create("$YEAR/$DAY-sample.in");

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