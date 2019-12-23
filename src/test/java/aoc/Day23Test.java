package aoc;

import org.junit.Test;

import static org.junit.Assert.*;

public class Day23Test {
  @Test
  public void testPart1() {
    assertEquals(16685, new Day23("day23.in").part1());
  }

  @Test
  public void testPart2() {
    assertEquals(11048, new Day23("day23.in").part2());
  }
}