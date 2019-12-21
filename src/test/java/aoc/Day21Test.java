package aoc;

import org.junit.Test;

import static org.junit.Assert.*;

public class Day21Test {
  @Test
  public void testPart1() {
    assertEquals(19348404, new Day21("day21.in").part1());
  }

  @Test
  public void testPart2() {
    assertEquals(1139206699, new Day21("day21.in").part2());
  }
}