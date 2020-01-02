package intcode.assembler;

import java.math.BigInteger;

class Constant implements ImmediateParameter {

  static final Constant MINUS_ONE = new Constant(BigInteger.valueOf(-1));
  static final Constant ZERO = new Constant(BigInteger.ZERO);

  private final BigInteger constant;

  public Constant(BigInteger value) {
    this.constant = value;
  }

  public Constant(int value) {
    this(BigInteger.valueOf(value));
  }

  @Override
  public ParameterMode mode() {
    return ParameterMode.IMMEDIATE;
  }

  @Override
  public BigInteger value() {
    return constant;
  }

}
