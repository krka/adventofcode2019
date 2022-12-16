package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day16Test {

  private Day day = new Day16("2022/day16.in");
  private Day sample = new Day16("2022/day16-sample.in");

  @Test
  public void testPart1() {
    assertEquals(1775, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(1651, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(2351, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(1707, sample.solvePart2());
  }
}