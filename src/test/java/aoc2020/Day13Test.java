package aoc2020;

import org.junit.Test;
import util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Day13Test {

  public static final String DAY = "13";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(119, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(295, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(new BigInteger("1106724616194525"), solvePart2(MAIN_INPUT));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(BigInteger.valueOf(1068781), solvePart2(SAMPLE_INPUT));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    int min = Integer.parseInt(input.get(0));
    List<Integer> buses = Stream.of(input.get(1).split(",")).filter(s -> !s.equals("x"))
            .map(Integer::parseInt).collect(Collectors.toList());

    int min2 = Integer.MAX_VALUE;
    int bestBus = -1;
    for (int bus : buses) {
      int wait = bus - (min % bus);
      if (wait < min2) {
        min2 = wait;
        bestBus = bus;
      }
    }
    return bestBus *  min2;
  }

  private BigInteger solvePart2(String name) {
    List<String> input = Util.readResource(name);

    // CRT:
    // https://medium.com/free-code-camp/how-to-implement-the-chinese-remainder-theorem-in-java-db88a3f1ffe0
    // https://en.wikipedia.org/wiki/Chinese_remainder_theorem
    BigInteger prod = BigInteger.ONE;

    List<BigInteger> buses = new ArrayList<>();
    List<BigInteger> remainder = new ArrayList<>();

    {
      int i = 0;
      for (String s : input.get(1).split(",")) {
        if (!s.equals("x")) {
          BigInteger bus = BigInteger.valueOf(Integer.parseInt(s));
          buses.add(bus);
          remainder.add(bus.subtract(BigInteger.valueOf(i)));
          prod = prod.multiply(bus);
        }
        i++;
      }
    }

    BigInteger sum = BigInteger.ZERO;
    int i = 0;
    for (BigInteger bus : buses) {
      BigInteger partial = prod.divide(bus);
      BigInteger inv = partial.modInverse(bus);
      BigInteger rem = remainder.get(i);

      sum = sum.add(partial.multiply(inv).multiply(rem));
      i++;
    }

    return sum.mod(prod);
  }
}
