package aoc2023;

import org.junit.Test;
import util.Day;
import util.TestBase;

import static org.junit.Assert.assertEquals;

public class Day19Test {

  private static final Day day = TestBase.create("$YEAR/$DAY.in");
  private static final Day sample = TestBase.create("$YEAR/$DAY-sample.in");

  @Test
  public void testPart1Sample() {
    assertEquals(19114, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(167409079868000L, sample.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(348378, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(121158073425385L, day.solvePart2());
  }
}