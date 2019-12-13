package aoc;

import aoc.Day8;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class Day8Test {
  @Test
  public void testPart1Sample() throws IOException {
    assertEquals(1, new Day8("day8-sample.in", 3, 2).part1());
  }

  @Test
  public void testPart1() throws IOException {
    assertEquals(1920, new Day8("day8.in", 25, 6).part1());
  }

  @Test
  public void testPart2() throws IOException {
    String s = new Day8("day8.in", 25, 6).part2();
    System.out.println(s);

    // Should say PCULA
  }
}