package aoc2022;

import org.junit.Test;
import util.Day;
import util.Util;

import static org.junit.Assert.assertEquals;

public class Day22Test {

  private Day day = new Day22("2022/day22.in");
  private Day sample = new Day22("2022/day22-sample.in");

  @Test
  public void testPart1() {
    final long actual = day.solvePart1();
    assertEquals(27436, actual);
  }

  @Test
  public void testPart1Sample() {
    assertEquals(6032, sample.solvePart1());
  }

  @Test
  public void testPart2() {
    final long actual = day.solvePart2();
    assertEquals(15426L, actual);
  }

  @Test
  public void testPart2Sample() {
    //assertEquals(5031, sample.solvePart2());
  }
}