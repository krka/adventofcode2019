package intcode.assembler;

import java.math.BigInteger;

class StackVariable extends Variable {
  private final int offset;

  public StackVariable(int offset) {
    super("int", "stack_" + offset, 1, new BigInteger[]{BigInteger.ZERO}, null, "# stack " + offset);
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

}
