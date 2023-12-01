package aoc2023;

import aoc2023.Day1;
import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day1Test {

  private Day day = new Day1("2023/day1.in");
  private Day sample = new Day1("2023/day1-sample.in");
  private Day sample2 = new Day1("2023/day1-sample2.in");

  @Test
  public void testPart1Sample() {
    assertEquals(142, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(281, sample2.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(55971, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(54719, day.solvePart2());
  }
}