package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day16Test {

  private static Day day = new Day16("2023/day16.in");
  private static Day sample = new Day16("2023/day16-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(46, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(51, sample.solvePart2());
  }


  @Test
  public void testPart1() {
    assertEquals(7210, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(7673, day.solvePart2());
  }
}