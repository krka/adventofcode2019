package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day2Test {

  private static Day day = new Day2("2023/day2.in");

  @Test
  public void testPart1() {
    assertEquals(2810, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(69110, day.solvePart2());
  }
}