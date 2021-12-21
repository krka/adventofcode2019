package aoc2021;

import intcode.IntCode;
import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day21Test {

  private static Day day = new Day21(7, 3);
  private static Day sample = new Day21(4, 8);

  @Test
  public void testPart1() {
    assertEquals(551901, day.solvePart1());
  }

  @Test
  public void testPart2() {
    for (int i = 0; i < 100; i++) {
      assertEquals(272847859601291L, ((Day) new Day21(7, 3)).solvePart2());
    }
  }

  @Test
  public void testPart1Sample() {
    assertEquals(739785, sample.solvePart1());
  }

  @Test
  public void testPart2Sample() {
    assertEquals(444356092776315L, sample.solvePart2());
  }

  @Test
  public void testIntcode() {
    AnnotatedIntCode compiled = Assembler.compileAnnotated("2021/day21.asm");
    System.out.println(compiled.toString());
    System.out.println("Compiled: " + compiled.toProgram());
    IntCode intCode = IntCode.fromResource(compiled.getIntCode());

    intCode.writeStdin(7);
    intCode.writeStdin(3);

    intCode.run();

    List<BigInteger> output = intCode.drainStdout();
    System.out.println("Output: " + output);
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(List.of(BigInteger.valueOf(272847859601291L)), output);
  }

}