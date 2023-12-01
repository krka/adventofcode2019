package aoc2023;

import intcode.IntCode;
import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;
import util.Day;
import util.Util;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day1Test {

  private Day day = new Day1("2023/day1.in");
  private Day sample = new Day1("2023/day1-sample.in");
  private Day sample2 = new Day1("2023/day1-sample2.in");

  @Test
  public void testPart1Sample() {
    assertEquals(142, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(281, sample2.solvePart2());
  }

  @Test
  public void testPart1() {
    assertEquals(55971, day.solvePart1());
  }
  @Test
  public void testPart2() {
    assertEquals(54719, day.solvePart2());
  }

  @Test
  public void testIntCode() {
    AnnotatedIntCode compiled = Assembler.compileAnnotated("2023/day1.asm");
    System.out.println(compiled.toString());
    System.out.println(compiled.toProgram());
    IntCode intCode = IntCode.fromResource(compiled.getIntCode());

    Util.readResource("2023/day1.in").forEach(intCode::writeASCIILine);
    intCode.run();

    final List<BigInteger> output = intCode.drainStdout();
    assertEquals(List.of(BigInteger.valueOf(55971), BigInteger.valueOf(54719)), output);
  }
}