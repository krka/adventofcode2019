package intcode.assembler;

import java.math.BigInteger;

class StackVariable extends Variable {
  private final int index;
  private int offset = -1;

  public StackVariable(int index) {
    super("int", "stack_" + index, 1, new BigInteger[]{BigInteger.ZERO}, null, "# stack " + index);
    this.index = index;
  }

  public StackVariable withOffset(int offset) {
    this.offset = offset;
    return this;
  }


  @Override
  public ParameterMode mode() {
    return ParameterMode.RELATIVE;
  }

  @Override
  public BigInteger value() {
    if (offset == -1) {
      throw new RuntimeException();
    }
    return BigInteger.valueOf(index - offset);
  }

}
