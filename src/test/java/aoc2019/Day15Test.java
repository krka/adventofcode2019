package aoc2019;

import aoc2019.Day15;
import org.junit.Test;

import static org.junit.Assert.*;

public class Day15Test {

  @Test
  public void testPart1() {
    assertEquals(258, new Day15("2019/day15.in").part1());
  }

  @Test
  public void testPart2() {
    assertEquals(372, new Day15("2019/day15.in").part2());
  }
}