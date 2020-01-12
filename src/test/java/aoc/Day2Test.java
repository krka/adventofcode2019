package aoc;

import intcode.IntCode;
import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;
import util.Util;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Day2Test {
  @Test
  public void testPart1() {
    AnnotatedIntCode annotatedIntCode = Assembler.compileAnnotated("day2-part1.asm");
    IntCode vm = IntCode.fromResource(annotatedIntCode.getIntCode());

    System.out.println(annotatedIntCode);

    List<BigInteger> input = Util.readResource("day2.in").stream()
            .flatMap(s -> Stream.of(s.split(",")))
            .filter(s -> !s.isEmpty())
            .map(BigInteger::new).collect(Collectors.toList());
    vm.writeStdin(input.size());
    input.forEach(vm::writeStdin);
    try {
      vm.run();
    } catch (Exception e) {
      System.out.println(vm.drainStdout());
      throw e;
    }
    assertEquals(IntCode.State.HALTED, vm.getState());
    assertEquals(5110675, vm.drainStdout().get(0).intValueExact());

    String program = annotatedIntCode.toProgram();
    System.out.println(program);
  }

  @Test
  public void testPart2() {
    AnnotatedIntCode annotatedIntCode = Assembler.compileAnnotated("day2-part2.asm");
    IntCode vm = IntCode.fromResource(annotatedIntCode.getIntCode());

    System.out.println(annotatedIntCode);

    List<BigInteger> input = Util.readResource("day2.in").stream()
            .flatMap(s -> Stream.of(s.split(",")))
            .filter(s -> !s.isEmpty())
            .map(BigInteger::new).collect(Collectors.toList());
    vm.writeStdin(input.size());
    input.forEach(vm::writeStdin);
    try {
      vm.run();
    } catch (Exception e) {
      System.out.println(vm.drainStdout());
      throw e;
    }
    assertEquals(IntCode.State.HALTED, vm.getState());
    assertEquals(4847, vm.drainStdout().get(0).intValueExact());

    String program = annotatedIntCode.toProgram();
    System.out.println(program);
  }

}