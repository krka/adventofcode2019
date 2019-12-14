package aoc;

import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class Day14Test {
  @Test
  public void testPart1Sample1() throws IOException {
    assertEquals(165, new Day14("day14-sample1.in").solve(BigInteger.ONE).intValueExact());
  }

  @Test
  public void testPart1() throws IOException {
    assertEquals(628586, new Day14("day14.in").solve(BigInteger.ONE).intValueExact());
  }

  @Test
  public void testPart2() throws IOException {
    assertEquals(3209254, new Day14("day14.in").solvePart2().intValueExact());
  }
}