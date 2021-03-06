package aoc2019;

import aoc2019.Day6;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day6Test {
  @Test
  public void testPart1Sample() {
    assertEquals(42, new Day6("2019/day6-sample.in").part1());
  }

  @Test
  public void testPart1() {
    assertEquals(144909, new Day6("2019/day6.in").part1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(4, new Day6("2019/day6-2-sample.in").part2());
  }

  @Test
  public void testPart2() {
    assertEquals(259, new Day6("2019/day6.in").part2());
  }
}