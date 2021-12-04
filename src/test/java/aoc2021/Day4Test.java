package aoc2021;

import intcode.IntCode;
import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;
import util.Util;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day4Test {
  @Test
  public void testPart1() {
    assertEquals(69579, new Day4("2021/day4.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(14877, new Day4("2021/day4.in").solvePart2());
  }

}