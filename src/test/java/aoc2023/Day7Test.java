package aoc2023;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day7Test {

  private static Day day = new Day7("2023/day7.in");
  private static Day sample = new Day7("2023/day7-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(6440, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(5905, sample.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(253313241, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(253362743, day.solvePart2());
  }
}