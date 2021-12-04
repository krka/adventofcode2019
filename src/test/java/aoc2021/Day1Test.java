package aoc2021;

import intcode.IntCode;
import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;
import util.Util;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day1Test {
  @Test
  public void testPart1() {
    assertEquals(1616, new Day1("2021/day1.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(1645, new Day1("2021/day1.in").solvePart2());
  }

  @Test
  public void testPart2IntCode() {
    AnnotatedIntCode compiled = Assembler.compileAnnotated("2021/day1.asm");
    System.out.println(compiled.toString());
    System.out.println(compiled.toProgram());
    IntCode intCode = IntCode.fromResource(compiled.getIntCode());

    Util.readResource("2021/day1.in").forEach(intCode::writeASCIILine);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(List.of("1645"), intCode.readAllASCIILines());
  }

}