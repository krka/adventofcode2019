package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day13Test {

  private static Day day = new Day13("2023/day13.in");
  private static Day sample = new Day13("2023/day13-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(405, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(400, sample.solvePart2());
  }


  @Test
  public void testPart1() {
    assertEquals(30535, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(30844, day.solvePart2());
  }
}