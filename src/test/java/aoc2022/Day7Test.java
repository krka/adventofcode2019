package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day7Test {

  private Day day = new Day7("2022/day7.in");

  @Test
  public void testPart1() {
    assertEquals(1077191, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(5649896, day.solvePart2());
  }
}