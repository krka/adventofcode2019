package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day15Test {

  private Day day = new Day15("2022/day15.in", 2000000);
  private Day sample = new Day15("2022/day15-sample.in", 10);

  @Test
  public void testPart1() {
    assertEquals(6124805, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(26, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(12555527364986L, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(56000011, sample.solvePart2());
  }
}