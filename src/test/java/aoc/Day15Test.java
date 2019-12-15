package aoc;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class Day15Test {

  @Test
  public void testPart1() {
    assertEquals(258, new Day15("day15.in").part1());
  }

  @Test
  public void testPart2() {
    assertEquals(372, new Day15("day15.in").part2());
  }
}