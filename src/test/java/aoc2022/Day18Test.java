package aoc2022;

import org.junit.Test;
import util.Day;
import util.Util;

import static org.junit.Assert.assertEquals;

public class Day18Test {

  private Day day = new Day18("2022/day18.in");
  private Day sample = new Day18("2022/day18-sample.in");

  @Test
  public void testPart1() {
    assertEquals(4310, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(64, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(2466, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(58, sample.solvePart2());
  }
}