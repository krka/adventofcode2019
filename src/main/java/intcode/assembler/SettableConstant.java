package intcode.assembler;

import java.math.BigInteger;

public class SettableConstant implements Parameter {
  private BigInteger value;

  public void setValue(BigInteger value) {
    this.value = value;
  }

  public void setValue(int value) {
    setValue(BigInteger.valueOf(value));
  }

  @Override
  public ParameterMode mode() {
    return ParameterMode.IMMEDIATE;
  }

  @Override
  public BigInteger value() {
    return value;
  }

  @Override
  public String toString() {
    return value().toString();
  }

}
