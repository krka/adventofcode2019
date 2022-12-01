package aoc2021;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day24Test {

  private static Day day = Day24.fromResource("2021/day24.in");

  @Test
  public void testPart1() {
    assertEquals(99995969919326L, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(48111514719111L, day.solvePart2());
  }


}