package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day12Test {

  private static Day day = new Day12("2023/day12.in");
  private static Day sample = new Day12("2023/day12-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(21, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(525152, sample.solvePart2());
  }


  @Test
  public void testPart1() {
    assertEquals(7017, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(527570479489L, day.solvePart2());
  }
}