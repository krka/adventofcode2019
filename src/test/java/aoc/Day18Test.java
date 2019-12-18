package aoc;

import org.junit.Test;

import static org.junit.Assert.*;

public class Day18Test {

  @Test
  public void testPart1Sample1() {
    assertEquals(86, new Day18("day18-sample1.in").part1());
  }

  @Test
  public void testPart1Sample2() {
    assertEquals(132, new Day18("day18-sample2.in").part1());
  }

  @Test
  public void testPart1Sample3() {
    assertEquals(136, new Day18("day18-sample3.in").part1());
  }

  @Test
  public void testPart1Sample4() {
    assertEquals(81, new Day18("day18-sample4.in").part1());
  }

  @Test
  public void testPart1() {
    assertEquals(5858, new Day18("day18.in").part1());
  }

  @Test
  public void testPart2() {
    assertEquals(2144, new Day18("day18.in").part2());
  }
}