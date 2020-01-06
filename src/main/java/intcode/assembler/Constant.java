package intcode.assembler;

import java.math.BigInteger;

public class Constant implements Parameter {

  public static final Constant MINUS_ONE = Constant.of(-1);
  public static final Constant ZERO = Constant.of(0);

  public static final Constant PLACEHOLDER_POSITION = Constant.of(ParameterMode.POSITION, BigInteger.ZERO);

  private final ParameterMode mode;
  private final BigInteger constant;

  private Constant(ParameterMode mode, BigInteger value) {
    this.mode = mode;
    this.constant = value;
  }

  public static Constant of(int value) {
    return Constant.of(ParameterMode.IMMEDIATE, value);
  }

  public static Constant of(BigInteger value) {
    return Constant.of(ParameterMode.IMMEDIATE, value);
  }

  public static Constant of(ParameterMode mode, int value) {
    return Constant.of(mode, BigInteger.valueOf(value));
  }

  public static Constant of(ParameterMode mode, BigInteger value) {
    return new Constant(mode, value);
  }

  @Override
  public ParameterMode mode() {
    return mode;
  }

  @Override
  public BigInteger value() {
    return constant;
  }

  @Override
  public String toString() {
    if (mode == ParameterMode.IMMEDIATE) {
      return value().toString();
    } else if (mode == ParameterMode.POSITION) {
      return "mem[" + value() + "]";
    } else {
      throw new RuntimeException();
    }
  }
}
