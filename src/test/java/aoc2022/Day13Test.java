package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day13Test {

  private Day day = new Day13("2022/day13.in");
  private Day sample = new Day13("2022/day13-sample.in");

  @Test
  public void testPart1() {
    assertEquals(5675, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(13, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(20383, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(140, sample.solvePart2());
  }
}