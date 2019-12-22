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
    return operations.stream().map(Day22::parse).collect(Collectors.toList());
  }

  private Operation merge(List<Operation> operations, long n) {
    return operations.stream().reduce((operation, operation2) -> operation.merge(operation2, n)).get();
  }

  private static Operation parse(String operation) {
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
    return merge(operations, n).apply(card, n);
  }

  public long part2(long n, long iterations, long cardPosition) {
    Operation operation = merge(operations, n);
    Operation inverted = operation.invert(n);

    for (int i = 0; i < 64; i++) {
      if (0 != (iterations & (1L << i))) {
        cardPosition = positive(multiply(inverted.factor, cardPosition, n) - inverted.offset, n);
      }
      inverted = inverted.merge(inverted, n);
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

    public Operation merge(Operation other, long n) {
      long factor = multiply(this.factor, other.factor, n);
      long offset = positive(multiply(this.offset, other.factor, n) + other.offset, n);
      return new Operation(factor, offset);
    }

    public Operation invert(long n) {
      long[] gcds = gcd(factor, n);
      long inverseFactor = gcds[1];

      long inverseOffset = multiply(offset, inverseFactor, n);
      return new Operation(inverseFactor, inverseOffset);
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
