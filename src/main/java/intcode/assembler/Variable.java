package intcode.assembler;

import java.math.BigInteger;

class Variable implements Parameter, HasAddress {
  private final int len;
  final BigInteger[] values;
  final HasAddress reference;
  private int address = -1;

  public Variable(int len, BigInteger[] values, HasAddress reference) {
    this.len = len;
    this.values = values;
    this.reference = reference;
  }

  public static Variable intVar(BigInteger value) {
    return new Variable(1, new BigInteger[]{value}, null);
  }

  public static Variable pointer(HasAddress reference) {
    return new Variable(1, null, reference);
  }

  public static Variable string(String s) {
    int len = s.length();
    BigInteger[] values = new BigInteger[len + 1];
    for (int i = 0; i < len; i++) {
      values[i] = BigInteger.valueOf(s.charAt(i));
    }
    values[len] = BigInteger.ZERO;
    return new Variable(len + 1, values, null);
  }

  public static Variable array(int len) {
    BigInteger[] values = new BigInteger[len];
    for (int i = 0; i < len; i++) {
      values[i] = BigInteger.ZERO;
    }
    return new Variable(len, values, null);
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
