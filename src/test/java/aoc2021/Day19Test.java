package aoc2021;

import org.junit.Test;
import util.Matrix3;

import static org.junit.Assert.assertEquals;

public class Day19Test {

  private static Day day = Day19.fromResource("2021/day19.in");

  @Test
  public void testPart1() {
    assertEquals(425, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(13354, day.solvePart2());
  }

}