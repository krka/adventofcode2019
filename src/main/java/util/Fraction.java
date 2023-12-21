package util;

import java.math.BigInteger;
import java.util.Objects;

public class Fraction implements Comparable<Fraction> {
  public static final Fraction ZERO = new Fraction(0, 1);
  public static final Fraction ONE = new Fraction(1, 1);

  final BigInteger a;
  final BigInteger b;

  private Fraction(long a, long b) {
    this.a = BigInteger.valueOf(a);
    this.b = BigInteger.valueOf(b);
  }

  private Fraction(BigInteger a, BigInteger b) {
    this.a = a;
    this.b = b;
  }

  public static Fraction of(BigInteger a, BigInteger b) {
    final int bZero = b.compareTo(BigInteger.ZERO);
    if (bZero < 0) {
      return of(a.negate(), b.negate());
    }
    if (bZero == 0) {
      throw new ArithmeticException("Division by zero");
    }
    if (a.compareTo(BigInteger.ZERO) == 0) {
      return ZERO;
    }
    BigInteger common = a.gcd(b);
    return new Fraction(a.divide(common), b.divide(common));
  }

  public static Fraction of(long a, long b) {
    return of(BigInteger.valueOf(a), BigInteger.valueOf(b));
  }

  public static Fraction of(long x) {
    return of(x, 1);
  }

  public Fraction mul(long other) {
    return of(a.multiply(BigInteger.valueOf(other)), b);
  }

  public Fraction mul(Fraction other) {
    return of(a.multiply(other.a), b.multiply(other.b));
  }

  public Fraction add(Fraction other) {
    final BigInteger newA = a.multiply(other.b).add(b.multiply(other.a));
    final BigInteger newB = b.multiply(other.b);
    return of(newA, newB);
  }

  public Fraction div(Fraction other) {
    return of(a.multiply(other.b), other.a.multiply(b));
  }

  public Fraction sub(Fraction other) {
    return add(other.negate());
  }

  private Fraction negate() {
    return of(a.negate(), b);
  }

  public Fraction abs() {
    if (a.compareTo(BigInteger.ZERO) >= 0) {
      return this;
    }
    return of(a.negate(), b);
  }

  @Override
  public int compareTo(Fraction other) {
    BigInteger left = a.multiply(other.b);
    BigInteger right = b.multiply(other.a);
    return left.compareTo(right);
  }

  public boolean isZero() {
    return a.compareTo(BigInteger.ZERO) == 0;
  }

  @Override
  public String toString() {
    if (b.equals(BigInteger.ONE)) {
      return a.toString();
    }
    return a + "/" + b;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Fraction fraction = (Fraction) o;
    return Objects.equals(a, fraction.a) && Objects.equals(b, fraction.b);
  }

  @Override
  public int hashCode() {
    return Objects.hash(a, b);
  }

  public long toLongExact() {
    if (b.equals(BigInteger.ONE)) {
      return a.longValueExact();
    }
    throw new RuntimeException("Denominator is not 1");
  }
}
