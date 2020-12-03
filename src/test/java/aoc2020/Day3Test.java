package aoc2020;

import intcode.IntCode;
import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;
import util.Util;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day3Test {
  @Test
  public void testPart1() {
    assertEquals(205, new Day3("2020/day3.in").solvePart1());
  }

  @Test
  public void testPart1IntCode() {
    AnnotatedIntCode compiled = Assembler.compileAnnotated("2020/day3.asm");
    System.out.println(compiled.toString());
    IntCode intCode = IntCode.fromResource(compiled.getIntCode());

    // Number of lines in input
    intCode.writeASCIILine("323");
    Util.readResource("2020/day3.in").forEach(intCode::writeASCIILine);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    List<String> output = intCode.readAllASCIILines();
    assertEquals(List.of("205"), output);
  }

  @Test
  public void testPart2() {
    assertEquals(3952146825L, new Day3("2020/day3.in").solvePart2());
  }

}