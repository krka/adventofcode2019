package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day9Test {

  private Day day = new Day9("2022/day9.in");
  private Day sample = new Day9("2022/day9-sample.in");

  @Test
  public void testPart1() {
    assertEquals(6269, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(13, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(2557, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(1, sample.solvePart2());
  }
}