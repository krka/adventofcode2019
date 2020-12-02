package aoc2020;

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
    assertEquals(270144, new Day1("2020/day1.in").solvePart1());
  }

  @Test
  public void testPart2() {
    assertEquals(261342720, new Day1("2020/day1.in").solvePart2());
  }

  @Test
  public void testPart2Rec() {
    assertEquals(261342720, new Day1("2020/day1.in").solvePart2Rec());
  }

  @Test
  public void testPart2IntCode() {
    AnnotatedIntCode compiled = Assembler.compileAnnotated("2020/day1.asm");
    System.out.println(compiled.toString());
    IntCode intCode = IntCode.fromResource(compiled.getIntCode());

    // Number of lines in input
    intCode.writeASCIILine("200");
    Util.readResource("2020/day1.in").forEach(intCode::writeASCIILine);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    List<String> output = intCode.readAllASCIILines();
    assertEquals(List.of("261342720"), output);
  }


}