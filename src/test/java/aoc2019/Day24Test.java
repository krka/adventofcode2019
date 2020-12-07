package aoc2019;

import aoc2019.Day24;
import org.junit.Test;

import static org.junit.Assert.*;

public class Day24Test {

  @Test
  public void testPart1Sample1() {
    assertEquals(2129920, new Day24("2019/day24-sample1.in").part1());
  }

  @Test
  public void testPart1() {
    assertEquals(30446641, new Day24("2019/day24.in").part1());
  }

  @Test
  public void testPart2() {
    assertEquals(1985, new Day24("2019/day24.in").part2());
  }
}