package aoc2022;

import org.junit.Test;
import util.Day;
import util.DayS;

import static org.junit.Assert.assertEquals;

public class Day25Test {

  private DayS day = new Day25("2022/day25.in");
  private DayS sample = new Day25("2022/day25-sample.in");

  @Test
  public void testPart1() {
    final String actual = day.solvePart1();
    assertEquals("2011-=2=-1020-1===-1", actual);
  }

  @Test
  public void testPart1Sample() {
    assertEquals("2=-1=0", sample.solvePart1());
  }

  @Test
  public void testPart2() {
    final String actual = day.solvePart2();
    assertEquals("", actual);
  }

  @Test
  public void testPart2Sample() {
    assertEquals("", sample.solvePart2());
  }
}