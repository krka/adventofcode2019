package aoc2023;

import org.junit.Test;
import util.Day;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static util.TestBase.create;

public class Day22Test {

  private static final Day day = create("$YEAR/$DAY.in");
  private static final Day sample = create("$YEAR/$DAY-sample.in");
  //private static final Day sample2 = create("$YEAR/$DAY-sample2.in");

  private static final Map<String, Long> EXPECTED = Map.of(
          "part1-sample", 5L,
          "part1", 407L,
          "part2-sample", 7L,
          "part2", 59266L,
          "<sentinel>", 0L
  );


  @Test
  public void testPart1Sample() {
    checkAnswer("part1-sample", sample.solvePart1());
  }

  @Test
  public void testPart1() {
    checkAnswer("part1", day.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    checkAnswer("part2-sample", sample.solvePart2());
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