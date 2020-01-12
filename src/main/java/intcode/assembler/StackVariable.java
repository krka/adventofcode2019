package intcode.assembler;

import java.math.BigInteger;

public class StackVariable extends Variable {
  private final String name;
  private final int index;

  public StackVariable(String name, int index) {
    super("int", name, 1, new BigInteger[]{BigInteger.ZERO}, null, "# stack " + index);
    this.name = name;
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

  @Override
  public String toString() {
    return name;
  }

}
