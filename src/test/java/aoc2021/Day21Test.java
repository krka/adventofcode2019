package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day21Test {

  private static Day day = new Day21(7, 3);
  private static Day sample = new Day21(4, 8);

  @Test
  public void testPart1() {
    assertEquals(551901, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(272847859601291L, day.solvePart2());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(739785, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(444356092776315L, sample.solvePart2());
  }

}