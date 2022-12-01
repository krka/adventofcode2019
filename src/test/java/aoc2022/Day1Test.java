package aoc2022;

import org.junit.Test;
import util.Day;

import static org.junit.Assert.*;

public class Day1Test {

  private Day day = new Day1("2022/day1.in");

  @Test
  public void testPart1() {
    assertEquals(70613, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(205805, day.solvePart2());
  }
}