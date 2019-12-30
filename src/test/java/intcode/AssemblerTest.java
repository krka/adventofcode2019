package intcode;

import intcode.assembler.Assembler;
import org.junit.Test;
import util.Util;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class AssemblerTest {
  @Test
  public void testSimple() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("simple.asm"));

    intCode.setDebugger(true);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(Arrays.asList(BigInteger.valueOf(123)), intCode.drainStdout());
  }

  @Test
  public void testFunction() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("function.asm"));

    intCode.setDebugger(true);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(Arrays.asList(BigInteger.valueOf(123)), intCode.drainStdout());
  }

  @Test
  public void testSwap() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("swap.asm"));

    intCode.setDebugger(true);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(Util.toBigInt(Arrays.asList(23, 100)), intCode.drainStdout());
  }

  @Test
  public void testArrays() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("arrays.asm"));

    intCode.setDebugger(true);
    intCode.run();
    assertEquals(IntCode.State.HALTED, intCode.getState());
    assertEquals(Util.toBigInt(Arrays.asList(1, 2, 20, 30)), intCode.drainStdout());
  }
}
