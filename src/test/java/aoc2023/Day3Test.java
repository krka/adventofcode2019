package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day3Test {

  private static Day day = new Day3("2023/day3.in");
  private static Day sample = new Day3("2023/day3-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(4361, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(467835, sample.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(557705, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(84266818, day.solvePart2());
  }
}