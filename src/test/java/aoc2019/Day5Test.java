package aoc2019;

import aoc2019.Day5;
import util.Util;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day5Test {
  @Test
  public void testPart1() {
    List<Integer> expected = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 16348437);

    assertEquals(Util.toBigInt(expected), Day5.part1());
  }

  @Test
  public void testPart2() {
    List<Integer> expected = Arrays.asList(6959377);

    assertEquals(Util.toBigInt(expected), Day5.part2());
  }
}