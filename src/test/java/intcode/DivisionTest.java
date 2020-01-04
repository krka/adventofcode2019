package intcode;

import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class DivisionTest {

  int numRuns = 0;
  long totalInstructions = 0;
  long maxInstructions = 0;
  private IntCode intCode;

  @Before
  public void setUp() {
    AnnotatedIntCode annotatedIntCode = Assembler.compileAnnotated("test_div.asm");
    System.out.println(annotatedIntCode.toString());
    intCode = IntCode.fromResource(annotatedIntCode.getIntCode());
    intCode.run();
  }

  @After
  public void tearDown() {
    System.out.println("Number of divisions: " + numRuns);
    System.out.printf(Locale.ROOT, "Average number of instructions: %.3f%n", totalInstructions / (double) numRuns);
    System.out.printf(Locale.ROOT, "Max number of instructions: %d%n", maxInstructions);
  }

  @Test
  public void testSimple() {
    intCode.setDebugger(true);
    assertDivision(intCode, 9, 2);
  }

  @Test
  public void testBig() {
    assertDivision(intCode, 23123123123L, 123123125L);
    assertDivision(intCode, Long.MAX_VALUE, Long.MAX_VALUE);
    assertDivision(intCode, Long.MAX_VALUE, Long.MAX_VALUE - 123);
    assertDivision(intCode, Long.MAX_VALUE - 123, Long.MAX_VALUE);
  }

  @Test
  public void testSmall() {
    for (int d = 2; d < 20; d++) {
      for (int n = 0; n < 50; n += 3) {
        assertDivision(intCode, n, d);
      }
    }
  }

  @Test
  public void testRandom() {
    Random random = new Random(1234L);
    for (int i = 0; i < 10000; i++) {
      assertDivision(intCode, Math.abs(random.nextLong()), Math.abs(random.nextLong()));
    }
  }

  private void assertDivision(IntCode intcode, long n, long d) {
    long q = n / d;
    long r = n % d;
    intcode.writeStdin(n);
    intcode.writeStdin(d);
    long before = intcode.getInstrCount();
    intcode.run();
    long after = intcode.getInstrCount();
    long numInstructions = after - before;

    totalInstructions += numInstructions;
    numRuns++;
    maxInstructions = Math.max(maxInstructions, numInstructions);

    List<BigInteger> answer = intcode.drainStdout();
    String message = String.format("%d / %d should be q=%d, r=%d", n, d, q, r);
    assertEquals(message, Util.toBigIntFromLong(Arrays.asList(q, r)), answer);
    //System.out.println(message + " - OK: " + numInstructions + " instructions");
  }
}
