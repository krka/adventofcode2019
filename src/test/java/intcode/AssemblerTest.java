package intcode;

import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;
import util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class AssemblerTest {
  @Test
  public void testSimple() {
    AnnotatedIntCode annotatedIntCode = Assembler.compileAnnotated("simple.asm");
    System.out.println(annotatedIntCode.toString());

    IntCode intCode = IntCode.fromResource(annotatedIntCode.getIntCode());

    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
  }

  @Test
  public void testFunction() {
    AnnotatedIntCode annotatedIntCode = Assembler.compileAnnotated("function.asm");
    System.out.println(annotatedIntCode.toString());
    List<BigInteger> compile = annotatedIntCode.getIntCode();
    IntCode intCode = IntCode.fromResource(compile);

    intCode.step(1000);
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(Util.toBigInt(Arrays.asList(123, 1234, 120, 720, -7429993)), intCode.drainStdout());
  }

  @Test
  public void testJumps() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("test_jumps.asm"));

    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
  }

  @Test
  public void testSwap() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("swap.asm"));
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
  }

  @Test
  public void testArrays() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("arrays.asm"));

    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(Util.toBigInt(Arrays.asList(30, 40, 30, 40)), intCode.drainStdout());
  }

  @Test
  public void testOutputs() {
    AnnotatedIntCode annotatedIntCode = Assembler.compileAnnotated("test_outputs.asm");
    System.out.println(annotatedIntCode);
    IntCode intCode = IntCode.fromResource(annotatedIntCode.getIntCode());
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    List<String> lines = intCode.readAllASCIILines();
    assertEquals(Arrays.asList("Hello World!", "Goodbye world", "Hello World!"), lines);
  }

  @Test
  public void testStdlib() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("test_stdlib.asm"));
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
  }

  @Test
  public void testMergeSort() {
    AnnotatedIntCode annotatedIntCode = Assembler.compileAnnotated("test_mergesort.asm");
    System.out.println(annotatedIntCode.toString());
    List<BigInteger> compile = annotatedIntCode.getIntCode();
    IntCode intCode = IntCode.fromResource(compile);
    Random random = new Random(1234L);

    int N = 100;

    intCode.writeStdin(N);
    ArrayList<BigInteger> input = new ArrayList<>();

    //intCode.setDebugger(true);

    intCode.run();

    for (int i = 0; i < N; i++) {
      BigInteger value = BigInteger.valueOf(random.nextLong());
      input.add(value);
      intCode.writeStdin(value);
    }

    System.out.println("input: " + input);
    input.sort(BigInteger::compareTo);
    System.out.println("expected: " + input);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    List<BigInteger> output = intCode.drainStdout();
    assertEquals(input, output);

  }
}
