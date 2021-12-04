package aoc2021;

import intcode.IntCode;
import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;
import util.Util;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day2Test {
  @Test
  public void testPart1() {
    assertEquals(2117664, new Day2("2021/day2.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(2073416724, new Day2("2021/day2.in").solvePart2());
  }

  @Test
  public void testPart2IntCode() {
    AnnotatedIntCode compiled = Assembler.compileAnnotated("2021/day2.asm");
    System.out.println(compiled.toString());
    System.out.println(compiled.toProgram());
    IntCode intCode = IntCode.fromResource(compiled.getIntCode());

    Util.readResource("2021/day2.in").forEach(intCode::writeASCIILine);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(List.of("2073416724"), intCode.readAllASCIILines());
  }

}