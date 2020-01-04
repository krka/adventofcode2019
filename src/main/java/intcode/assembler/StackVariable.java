package intcode.assembler;

import java.math.BigInteger;

class StackVariable extends Variable {
  private final int index;

  public StackVariable(int index) {
    super("int", "stack_" + index, 1, new BigInteger[]{BigInteger.ZERO}, null, "# stack " + index);
    this.index = index;
  }

  @Override
  public ParameterMode mode() {
    return ParameterMode.RELATIVE;
  }

  @Override
  public BigInteger value() {
    return BigInteger.valueOf(index);
  }

}
