package aoc2020;

import org.junit.Test;
import util.Util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class Day25Test {

  public static final String DAY = "25";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";
  public static final BigInteger SEVEN = BigInteger.valueOf(7);

  @Test
  public void testPart1() {
    assertEquals(17980581L, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(14897079L, solvePart1(SAMPLE_INPUT));
  }

  static BigInteger M = BigInteger.valueOf(20201227L);

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    BigInteger pub1 = new BigInteger(input.get(0));
    BigInteger pub2 = new BigInteger(input.get(1));

    BigInteger cur = BigInteger.valueOf(1);
    for (int i = 1; true; i++) {
      cur = cur.multiply(SEVEN).mod(M);
      if (cur.equals(pub1)) {
        return fun(pub2, i).longValueExact();
      }
      if (cur.equals(pub2)) {
        return fun(pub1, i).longValueExact();
      }
    }
  }

  private BigInteger fun(BigInteger subj, long loop) {
    return subj.modPow(BigInteger.valueOf(loop), M);
  }

}
