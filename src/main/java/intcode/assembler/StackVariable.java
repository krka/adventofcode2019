package intcode.assembler;

import java.math.BigInteger;

class StackVariable implements Parameter {
  private final int offset;

  public StackVariable(int offset) {
    this.offset  = offset;
  }

  @Override
  public ParameterMode mode() {
    return ParameterMode.RELATIVE;
  }

  @Override
  public BigInteger value() {
    return BigInteger.valueOf(offset);
  }

  @Override
  public ImmediateParameter derefence() {
    throw new RuntimeException("can't dereference a stack variable");
  }
}
