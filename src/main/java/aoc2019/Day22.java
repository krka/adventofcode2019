package aoc2019;

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

    private static long positive(long x, long n) {
      return ((x % n) + n) % n;
    }

    public Operation merge(Operation other) {
      if (n != other.n) {
        throw new RuntimeException();
      }
      long factor = multiply(this.factor, other.factor, n);
      long offset = multiply(this.offset, other.factor, n) + other.offset;
      return new Operation(n, factor, offset);
    }

    public Operation invert() {
      Util.GcdResult gcds = Util.gcdExtended(factor, n);
      long inverseFactor = gcds.a;

      long inverseOffset = multiply(offset, inverseFactor, n);
      return new Operation(n, inverseFactor, n - inverseOffset);
    }
  }

}
