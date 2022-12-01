package aoc2021;

import intcode.IntCode;
import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;
import util.Day;
import util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day20Test {

  private static Day day = Day20.fromResource("2021/day20.in");
  private static Day sample = Day20.fromResource("2021/day20-sample.in");

  @Test
  public void testPart1() {
    assertEquals(5301, day.solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(19492, day.solvePart2());
  }

  @Test
  public void testPart1Sample() {
    assertEquals(35, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(3351, sample.solvePart2());
  }

  @Test
  public void testIntcode() {
    AnnotatedIntCode compiled = Assembler.compileAnnotated("2021/day20.asm");
    System.out.println(compiled.toString());
    System.out.println("Compiled: " + compiled.toProgram());
    IntCode intCode = IntCode.fromResource(compiled.getIntCode());

    Util.readResource("2021/day20.in").forEach(intCode::writeASCIILine);

    List<BigInteger> output = new ArrayList<>();
    intCode.step(10000);
    while (intCode.getState() == IntCode.State.PAUSED) {
      List<BigInteger> out = intCode.drainStdout();
      output.addAll(out);
      out.forEach(System.out::println);
      intCode.step(10000);
    }

    output.addAll(intCode.drainStdout());
    System.out.println("Output: " + output);
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(List.of(BigInteger.valueOf(5301), BigInteger.valueOf(19492)), output);
  }
}