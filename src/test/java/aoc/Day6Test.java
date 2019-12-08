package aoc;

import aoc.Day6;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Day6Test {
  @Test
  public void testPart1Sample() throws IOException {
    assertEquals(42, new Day6("day6-sample.in").part1());
  }

  @Test
  public void testPart1() throws IOException {
    assertEquals(144909, new Day6("day6.in").part1());
  }

  @Test
  public void testPart2Sample() throws IOException {
    assertEquals(4, new Day6("day6-2-sample.in").part2());
  }

  @Test
  public void testPart2() throws IOException {
    assertEquals(259, new Day6("day6.in").part2());
  }
}