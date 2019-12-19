package aoc;

import org.junit.Test;

import static org.junit.Assert.*;

public class Day19Test {

  @Test
  public void testPart1() {
    assertEquals(158, new Day19("day19.in").part1());
  }

  @Test
  public void testPart2() {
    assertEquals(6191165, new Day19("day19.in").part2());
  }
}