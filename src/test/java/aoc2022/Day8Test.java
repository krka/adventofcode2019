package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day8Test {

  private Day day = new Day8("2022/day8.in");
  private Day sample = new Day8("2022/day8-sample.in");

  @Test
  public void testPart1() {
    assertEquals(1827, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(21, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(335580, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(8, sample.solvePart2());
  }
}