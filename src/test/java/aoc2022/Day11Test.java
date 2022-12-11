package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day11Test {

  private Day day = new Day11("2022/day11.in");
  private Day sample = new Day11("2022/day11-sample.in");

  @Test
  public void testPart1() {
    assertEquals(61005, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(10605, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(20567144694L, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(2713310158L, sample.solvePart2());
  }
}