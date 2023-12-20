package aoc2023;

import org.junit.Test;
import util.Day;
import util.TestBase;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static util.TestBase.create;

public class Day20Test {

  private static final Day day = create("$YEAR/$DAY.in");
  private static final Day sample = create("$YEAR/$DAY-sample.in");
  private static final Day sample2 = create("$YEAR/$DAY-sample2.in");

  private static final Map<String, Long> EXPECTED = Map.of(
          "part1-sample", 32000000L,
          "part1-sample2", 11687500L,
          "part1", 899848294L,
          "part2", 247454898168563L,
          "<sentinel>", 0L
  );


  @Test
  public void testPart1Sample() {
    checkAnswer("part1-sample", sample.solvePart1());
  }

  @Test
  public void testPart1Sample2() {
    checkAnswer("part1-sample2", sample2.solvePart1());
  }

  @Test
  public void testPart1() {
    checkAnswer("part1", day.solvePart1());
  }

  @Test
  public void testPart2() {
    checkAnswer("part2", day.solvePart2());
  }

  private void checkAnswer(String key, long actual) {
    final Long expectedValue = EXPECTED.get(key);
    if (expectedValue == null) {
      fail("Missing expected value for " + key);
    }
    assertEquals(key, expectedValue.longValue(), actual);
  }
}