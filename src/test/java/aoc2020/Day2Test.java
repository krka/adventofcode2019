package aoc2020;

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
    assertEquals(542, new Day2("2020/day2.in").solvePart1());
  }

  @Test
  public void testPart1IntCode() {
    AnnotatedIntCode compiled = Assembler.compileAnnotated("2020/day2.asm");
    System.out.println(compiled.toString());
    IntCode intCode = IntCode.fromResource(compiled.getIntCode());

    // Number of lines in input
    intCode.writeASCIILine("1000");
    Util.readResource("2020/day2.in").forEach(intCode::writeASCIILine);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    List<String> output = intCode.readAllASCIILines();
    assertEquals(List.of("542"), output);
  }

  @Test
  public void testPart2() {
    assertEquals(360, new Day2("2020/day2.in").solvePart2());
  }


}