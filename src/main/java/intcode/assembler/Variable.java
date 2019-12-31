package intcode.assembler;

import java.math.BigInteger;

class Variable implements Parameter, HasAddress {
  private final int len;
  final BigInteger[] values;
  private int address = -1;

  public Variable(int len) {
    this.len = len;
    this.values = new BigInteger[len];
  }

  public Variable(BigInteger[] values) {
    this.len = values.length;
    this.values = values;
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
    return BigInteger.valueOf(getAddress());
  }

  @Override
  public ImmediateParameter dereference() {
    return new DeferredConstant(this);
  }

  public void setAddress(int address) {
    this.address = address;
  }

  @Override
  public int getAddress() {
    if (address == -1) {
      throw new RuntimeException("Can't resolve it before it has been set");
    }
    return address;
  }
}
