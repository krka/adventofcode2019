package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day17Test {

  private Day day = new Day17("2022/day17.in");
  private Day sample = new Day17("2022/day17-sample.in");

  @Test
  public void testPart1() {
    assertEquals(0, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(0, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(0, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(0, sample.solvePart2());
  }
}