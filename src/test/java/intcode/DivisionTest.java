package intcode;

import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    assertDivision(intCode, 9, 2);
    assertDivision(intCode, -9, 2);
    assertDivision(intCode, 9, -2);
    assertDivision(intCode, -9, -2);
  }

  @Test(expected = RuntimeException.class)
  public void testCrashOnNaN() {
    intCode.writeStdin((long) 123);
    intCode.writeStdin((long) 0);
    intCode.run();
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
  public void testReallyBig() {
    BigInteger n = new BigInteger("17");
    BigInteger d = new BigInteger("3");
    for (int i = 0; i < 200; i++) {
      System.out.println("Testing division where n has " + n.bitLength() + " bits");
      assertDivision(intCode, n, d);
      n = n.multiply(BigInteger.TWO);
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
    assertDivision(intcode, BigInteger.valueOf(n), BigInteger.valueOf(d));
  }

  private void assertDivision(IntCode intcode, BigInteger n, BigInteger d) {
    BigInteger q = n.divide(d);
    BigInteger r = n.remainder(d);
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
    assertEquals(message, Arrays.asList(q, r), answer);
    //System.out.println(message + " - OK: " + numInstructions + " instructions");
  }
}
