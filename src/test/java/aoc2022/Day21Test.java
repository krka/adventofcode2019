package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.assertEquals;

public class Day21Test {

  private Day day = new Day21("2022/day21.in");
  private Day sample = new Day21("2022/day21-sample.in");

  @Test
  public void testPart1() {
    final long actual = day.solvePart1();
    assertEquals(70674280581468L, actual);
  }

  @Test
  public void testPart1Sample() {
    assertEquals(152, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(3243420789721L, day.solvePart2());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(301, sample.solvePart2());
  }
}