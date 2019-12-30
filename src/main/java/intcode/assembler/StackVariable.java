package intcode.assembler;

import java.math.BigInteger;

class StackVariable extends Variable {
  private final int offset;

  public StackVariable(int offset) {
    super(1);
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
  public ImmediateParameter dereference() {
    throw new RuntimeException("can't dereference a stack variable");
  }
}
