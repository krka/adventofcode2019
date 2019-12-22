package aoc;

import util.Util;

import java.math.BigInteger;

public class Day22 {
  private final String name;

  public Day22(String name) {
    this.name = name;
  }

  private static Operation parse(String operation, long n) {
    String[] words = operation.split(" ");
    if (words[1].equals("into")) {
      return Operation.reverse(n);
    } else if (words[1].equals("with")) {
      return Operation.incrMod(Integer.parseInt(words[3]), n);
    } else if (words[0].equals("cut")) {
      return Operation.rotate(Integer.parseInt(words[1]), n);
    }
    throw new RuntimeException(operation);
  }

  private Operation readOperations(long n) {
    return Util.readResource(name).stream()
            .map(operation -> parse(operation, n))
            .reduce(Operation::merge)
            .get();
  }

  long part1(long card, long n) {
    return readOperations(n).apply(card);
  }

  public long part2(long n, long iterations, long cardPosition) {
    return readOperations(n)
            .power(iterations)
            .invert()
            .apply(cardPosition);
  }

  private static long multiply(long a, long b, long n) {
    return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(n)).longValueExact();
  }

  private static long positive(long x, long n) {
    return ((x % n) + n) % n;
  }

  private static class Operation {
    final long n;
    final long factor;
    final long offset;

    public Operation(long n, long factor, long offset) {
      this.n = n;
      this.factor = factor;
      this.offset = offset;
    }

    public static Operation reverse(long n) {
      return new Operation(n, -1, -1);
    }

    public static Operation incrMod(int incr, long n) {
      return new Operation(n, incr, 0);
    }

    public static Operation rotate(int number, long n) {
      return new Operation(n, 1, -number);
    }

    public Operation power(long exponent) {
      Operation res = new Operation(n, 1, 0);
      Operation doubled = this;
      for (int i = 0; i < 64; i++) {
        if (0 != (exponent & (1L << i))) {
          res = res.merge(doubled);
        }
        doubled = doubled.merge(doubled);
      }
      return res;
    }

    public long apply(long card) {
      return positive(multiply(card, factor, n) + offset, n);
    }

    public Operation merge(Operation other) {
      if (n != other.n) {
        throw new RuntimeException();
      }
      long factor = multiply(this.factor, other.factor, n);
      long offset = positive(multiply(this.offset, other.factor, n) + other.offset, n);
      return new Operation(n, factor, offset);
    }

    public Operation invert() {
      long[] gcds = gcd(factor, n);
      long inverseFactor = gcds[1];

      long inverseOffset = multiply(offset, inverseFactor, n);
      return new Operation(n, inverseFactor, n - inverseOffset);
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
