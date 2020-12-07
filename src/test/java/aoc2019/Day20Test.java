package aoc2019;

import aoc2019.Day20;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day20Test {

  @Test
  public void testPart1Sample1() {
    assertEquals(23, new Day20("2019/day20-sample1.in").part1());
  }

  @Test
  public void testPart1Sample2() {
    assertEquals(58, new Day20("2019/day20-sample2.in").part1());
  }

  @Test
  public void testPart1() {
    assertEquals(484, new Day20("2019/day20.in").part1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(396, new Day20("2019/day20-sample3.in").part2());
  }

  @Test
  public void testPart2() {
    assertEquals(5754, new Day20("2019/day20.in").part2());
  }
}