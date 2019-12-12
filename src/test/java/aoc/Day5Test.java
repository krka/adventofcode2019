package aoc;

import aoc.Day5;
import aoc.Util;
import intcode.Decompiler;
import intcode.IntCode;
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