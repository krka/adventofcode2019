package aoc;

import util.Util;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class Day22 {
  private final String name;
  private final List<Operation> operations;

  public Day22(String name) {
    this.name = name;
    operations = parse(Util.readResource(name));
  }

  private List<Operation> parse(List<String> operations) {
    return operations.stream().map(Day22::parse1).collect(Collectors.toList());
  }

  private static Operation parse1(String operation) {
    String[] words = operation.split(" ");
    if (words[1].equals("into")) {
      return Operation.reverse();
    } else if (words[1].equals("with")) {
      return Operation.incrMod(Integer.parseInt(words[3]));
    } else if (words[0].equals("cut")) {
      return Operation.rotate(Integer.parseInt(words[1]));
    }
    throw new RuntimeException(operation);
  }

  private long apply(long card, long n) {
    for (Operation operation : operations) {
      card = operation.apply(card, n);
    }
    return card;
  }

  long part1(long card, long n) {
    return apply(card, n);
  }

  public long part2(long n, long iterations, long cardPosition) {
    long c0 = apply(0, n);
    long c1 = apply(1, n);

    long factor = positive(c1 - c0, n);

    long[] gcds = gcd(factor, n);
    long inverse = gcds[1];

    long invC0 = multiply(c0, inverse, n);

    // reverse application:
    // next_card = inverse * card - invC0
    // answer = apply(cardPosition, inverse) * iterations times

    long[] factors = new long[64];
    long[] offsets = new long[64];
    factors[0] = inverse;
    offsets[0] = invC0;
    for (int i = 1; i < 64; i++) {
      long k = factors[i - 1];
      long c = offsets[i - 1];
      factors[i] = multiply(k, k, n);
      offsets[i] = multiply(positive(k + 1, n), c, n);
    }

    for (int i = 0; i < 64; i++) {
      if (0 != (iterations & (1L << i))) {
        cardPosition = positive(multiply(factors[i], cardPosition, n) - offsets[i], n);
      }
    }
    return cardPosition;
  }

  private static long multiply(long a, long b, long n) {
    return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(n)).longValueExact();
  }

  private static long positive(long x, long n) {
    return ((x % n) + n) % n;
  }

  private static class Operation {
    final long factor;
    final long offset;

    public Operation(long factor, long offset) {
      this.factor = factor;
      this.offset = offset;
    }

    public static Operation reverse() {
      return new Operation(-1, -1);
    }

    public static Operation incrMod(int incr) {
      return new Operation(incr, 0);
    }

    public static Operation rotate(int number) {
      return new Operation(1, -number);
    }

    public long apply(long card, long n) {
      return positive(multiply(card, factor, n) + offset, n);
    }
  }

  //  return array [d, a, b] such that d = gcd(p, q), ap + bq = d
  static long[] gcd(long p, long q) {
    if (q == 0)
      return new long[] { p, 1, 0 };

    long[] vals = gcd(q, p % q);
    long d = vals[0];
    long a = vals[2];
    long b = vals[1] - (p / q) * vals[2];
    return new long[] { d, a, b };
  }
}
