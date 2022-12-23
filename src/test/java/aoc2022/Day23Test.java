package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day23Test {

  private Day day = new Day23("2022/day23.in");
  private Day sample = new Day23("2022/day23-sample.in");

  @Test
  public void testPart1() {
    final long actual = day.solvePart1();
    assertEquals(4123, actual);
  }

  @Test
  public void testPart1Sample() {
    assertEquals(110, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    final long actual = day.solvePart2();
    assertEquals(1029, actual);
  }

  @Test
  public void testPart2Sample() {
    assertEquals(20, sample.solvePart2());
  }
}