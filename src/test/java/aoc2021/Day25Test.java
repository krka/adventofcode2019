package aoc2021;

import intcode.IntCode;
import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;
import util.Day;
import util.Util;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day25Test {

  private static Day day = Day25.fromResource("2021/day25.in");
  private static Day sample = Day25.fromResource("2021/day25-sample.in");

  @Test
  public void testPart1() {
    assertEquals(308, day.solvePart1());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(58, sample.solvePart1());
  }

  @Test
  public void testIntcode() {
    AnnotatedIntCode compiled = Assembler.compileAnnotated("2021/day25.asm");
    System.out.println(compiled.toString());
    System.out.println("Compiled: " + compiled.toProgram());
    IntCode intCode = IntCode.fromResource(compiled.getIntCode());

    Util.readResource("2021/day25.in").forEach(intCode::writeASCIILine);

    intCode.run();

    List<BigInteger> output = intCode.drainStdout();
    System.out.println("Output: " + output);
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(List.of(BigInteger.valueOf(308)), output);
  }

}