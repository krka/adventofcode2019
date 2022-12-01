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

public class Day16Test {

  private Day day = Day16.fromResource("2021/day16.in");

  @Test
  public void testPart1() {
    assertEquals(860, day.solvePart1());
  }

  @Test
  public void testPart2() {
    long t1 = System.nanoTime();
    long actual = day.solvePart2();
    long t2 = System.nanoTime();
    System.out.println(t2 - t1);
    assertEquals(470949537659L, actual);
  }

  @Test
  public void testIntcode() {
    AnnotatedIntCode compiled = Assembler.compileAnnotated("2021/day16.asm");
    System.out.println(compiled.toString());
    System.out.println("Compiled:" + compiled.toProgram());
    IntCode intCode = IntCode.fromResource(compiled.getIntCode());

    Util.readResource("2021/day16.in").forEach(intCode::writeASCIILine);
    intCode.run();
    List<BigInteger> output = intCode.drainStdout();
    System.out.println("Output:" + output);
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(List.of(BigInteger.valueOf(860), BigInteger.valueOf(470949537659L)), output);
  }

}