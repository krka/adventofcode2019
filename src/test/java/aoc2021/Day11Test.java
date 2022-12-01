package aoc2021;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day11Test {

  private Day day = new Day11("2021/day11.in");

  @Test
  public void testPart1() {
    assertEquals(1632, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(303, day.solvePart2());
  }

}