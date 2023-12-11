package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day11Test {

  private static Day day = new Day11("2023/day11.in");
  private static Day sample = new Day11("2023/day11-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(374, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(82000210, sample.solvePart2());
  }


  @Test
  public void testPart1() {
    assertEquals(9693756, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(717878258016L, day.solvePart2());
  }
}