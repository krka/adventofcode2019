package aoc;

import intcode.IntCode;
import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;
import util.Util;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Day1Test {
  @Test
  public void testPart1() {
    AnnotatedIntCode annotatedIntCode = Assembler.compileAnnotated("day1-part1.asm");
    IntCode vm = IntCode.fromResource(annotatedIntCode.getIntCode());

    List<BigInteger> input = Util.readResource("day1.in").stream()
            .filter(s -> !s.isEmpty())
            .map(BigInteger::new).collect(Collectors.toList());
    vm.writeStdin(input.size());
    input.forEach(vm::writeStdin);
    vm.run();
    assertEquals(IntCode.State.HALTED, vm.getState());
    assertEquals(3404722, vm.drainStdout().get(0).intValueExact());

    System.out.println(annotatedIntCode);
    String program = annotatedIntCode.getIntCode().stream().map(Object::toString).collect(Collectors.joining(","));
    System.out.println(program);
  }

  @Test
  public void testPart2() {
    AnnotatedIntCode annotatedIntCode = Assembler.compileAnnotated("day1-part2.asm");
    IntCode vm = IntCode.fromResource(annotatedIntCode.getIntCode());

    List<BigInteger> input = Util.readResource("day1.in").stream()
            .filter(s -> !s.isEmpty())
            .map(BigInteger::new).collect(Collectors.toList());
    vm.writeStdin(input.size());
    input.forEach(vm::writeStdin);
    vm.run();
    assertEquals(IntCode.State.HALTED, vm.getState());
    assertEquals(5104215, vm.drainStdout().get(0).intValueExact());

    System.out.println(annotatedIntCode);
    String program = annotatedIntCode.getIntCode().stream().map(Object::toString).collect(Collectors.joining(","));
    System.out.println(program);
  }
}