package aoc2019;

import aoc2019.Day7;
import aoc2019.Day7Part2;
import org.junit.Test;

import static org.junit.Assert.*;

public class Day7Test {
  @Test
  public void testPart1Sample1() {
    assertEquals(43210, new Day7("2019/day7-sample1.in").solve());
  }

  @Test
  public void testPart1Sample2() {
    assertEquals(54321, new Day7("2019/day7-sample2.in").solve());
  }

  @Test
  public void testPart1Sample3() {
    assertEquals(65210, new Day7("2019/day7-sample3.in").solve());
  }

  @Test
  public void testPart1() {
    assertEquals(11828, new Day7("2019/day7.in").solve());
  }

  @Test
  public void testPart2() {
    assertEquals(1714298, new Day7Part2("2019/day7.in").solve());
  }
}