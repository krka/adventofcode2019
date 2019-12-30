package intcode.assembler;

import java.math.BigInteger;

class Variable implements Parameter {
  private final int len;
  private int address = -1;

  public Variable(int len) {
    this.len = len;
  }

  public int getLen() {
    return len;
  }

  @Override
  public ParameterMode mode() {
    return ParameterMode.POSITION;
  }

  @Override
  public BigInteger value() {
    if (address == -1) {
      throw new RuntimeException("Can't resolve it before it has been set");
    }
    return BigInteger.valueOf(address);
  }

  public void setAddress(int address) {
    this.address = address;
  }
}
