package aoc2019;

import aoc2019.Day17;
import org.junit.Test;

import static org.junit.Assert.*;

public class Day17Test {
  @Test
  public void testPart1() {
    assertEquals(4044, new Day17("2019/day17.in").part1());
  }

  @Test
  public void testPart2() {
    assertEquals(893283, new Day17("2019/day17.in").part2());
  }
}