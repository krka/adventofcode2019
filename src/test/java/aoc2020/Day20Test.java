package aoc2020;

import org.junit.Test;
import util.Util;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day20Test {

  public static final String DAY = "20";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(0, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(0, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(0, solvePart2(MAIN_INPUT));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(0, solvePart2(SAMPLE_INPUT));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    return 0;
  }


  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);
    return 0;
  }


}
