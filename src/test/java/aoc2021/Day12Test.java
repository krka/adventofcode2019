package aoc2021;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day12Test {

  private Day day = new Day12("2021/day12.in");

  @Test
  public void testPart1() {
    assertEquals(4413, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(118803, day.solvePart2());
  }

}