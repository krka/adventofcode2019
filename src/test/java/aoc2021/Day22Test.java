package aoc2021;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day22Test {

  private static Day day = Day22.fromResource("2021/day22.in");

  @Test
  public void testPart1() {
    assertEquals(547648, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(1206644425246111L, day.solvePart2());
  }


}