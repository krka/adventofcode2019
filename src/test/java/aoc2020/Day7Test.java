package aoc2020;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day7Test {
  @Test
  public void testPart1() {
    assertEquals(205, new Day7("2020/day7.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(80902, new Day7("2020/day7.in").solvePart2());
  }

  @Test
  public void testPart2Sample1() {
    assertEquals(126, new Day7("2020/day7-sample1.in").solvePart2());
  }

}