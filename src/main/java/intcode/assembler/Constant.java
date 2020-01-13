package intcode.assembler;

import java.math.BigInteger;

public class Constant implements Parameter {

  public static final Constant MINUS_ONE = Constant.of(-1);
  public static final Constant ZERO = Constant.of(0);
  public static final Constant ONE = Constant.of(1);

  private final BigInteger constant;

  private Constant(BigInteger value) {
    this.constant = value;
  }

  public static Constant of(int value) {
    return new Constant(BigInteger.valueOf(value));
  }

  public static Constant of(BigInteger value) {
    return new Constant(value);
  }

  @Override
  public ParameterMode mode() {
    return ParameterMode.IMMEDIATE;
  }

  @Override
  public BigInteger value() {
    return constant;
  }

  @Override
  public String toString() {
    return value().toString();
  }
}
