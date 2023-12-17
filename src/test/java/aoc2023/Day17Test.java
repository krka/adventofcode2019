package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day17Test {

  private static Day day = new Day17("2023/day17.in");
  private static Day sample = new Day17("2023/day17-sample.in");
  private static Day sample2 = new Day17("2023/day17-sample2.in");

  @Test
  public void testPart1Sample() {
    assertEquals(102, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(94, sample.solvePart2());
  }

  @Test
  public void testPart2Sample2() {
    assertEquals(71, sample2.solvePart2());
  }


  @Test
  public void testPart1() {
    assertEquals(1065, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(1249, day.solvePart2());
  }
}