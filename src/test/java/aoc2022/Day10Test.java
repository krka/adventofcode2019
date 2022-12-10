package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day10Test {

  private Day day = new Day10("2022/day10.in");
  private Day sample = new Day10("2022/day10-sample.in");

  @Test
  public void testPart1() {
    assertEquals(14520, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(13140, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    // Output: PZBGZEJB
    assertEquals(0, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(0, sample.solvePart2());
  }
}