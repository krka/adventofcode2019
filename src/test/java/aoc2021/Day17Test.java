package aoc2021;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day17Test {

  private Day day = Day17.fromResource("2021/day17.in");
  private Day sample = new Day17("target area: x=20..30, y=-10..-5");

  @Test
  public void testPart1() {
    assertEquals(4005, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(2953, day.solvePart2());
  }

  @Test
  public void testSamplePart1() {
    assertEquals(45, sample.solvePart1());
  }

  @Test
  public void testSamplePart2() {
    assertEquals(112, sample.solvePart2());
  }

}