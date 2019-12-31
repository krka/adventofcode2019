package intcode;

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
    IntCode intCode = IntCode.fromResource(Assembler.compile("simple.asm"));

    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(Arrays.asList(BigInteger.valueOf(123)), intCode.drainStdout());
  }

  @Test
  public void testFunction() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("function.asm"));

    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(Arrays.asList(BigInteger.valueOf(123)), intCode.drainStdout());
  }

  @Test
  public void testSwap() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("swap.asm"));

    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(Util.toBigInt(Arrays.asList(23, 100)), intCode.drainStdout());
  }

  @Test
  public void testArrays() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("arrays.asm"));

    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(Util.toBigInt(Arrays.asList(1, 2, 20, 30)), intCode.drainStdout());
  }

  @Test
  public void testOutputs() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("test_outputs.asm"));
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    List<String> lines = intCode.readAllASCIILines();
    assertEquals(Arrays.asList("Hello World!"), lines);
  }

  @Test
  public void testSort() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("test_sort.asm"));
    Random random = new Random(1234L);

    ArrayList<BigInteger> input = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      BigInteger value = BigInteger.valueOf(random.nextLong());
      input.add(value);
      intCode.writeStdin(value);
    }

    input.sort(BigInteger::compareTo);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    List<BigInteger> output = intCode.drainStdout();
    assertEquals(input, output);

  }
}
