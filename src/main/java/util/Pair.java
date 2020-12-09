package util;

import java.util.Objects;

public class Pair<A, B> {
  private final A a;
  private final B b;

  public Pair(A a, B b) {
    this.a = a;
    this.b = b;
  }

  public static <A, B> Pair<A, B> of(A a, B b) {
    return new Pair<>(a, b);
  }

  public static Pair<Integer, Integer> fromInt(String s) {
    String[] split = s.split("[, ]+");
    return Pair.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
  }

  public A a() {
    return a;
  }

  public B b() {
    return b;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(a, pair.a) &&
            Objects.equals(b, pair.b);
  }

  @Override
  public int hashCode() {
    return Objects.hash(a, b);
  }

  @Override
  public String toString() {
    return "(" +
            "" + a +
            ", " + b +
            ')';
  }
}
