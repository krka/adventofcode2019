package intcode.assembler;

import java.math.BigInteger;
import java.util.concurrent.Callable;

class Variable implements Parameter {
  final String type;
  private final String name;
  private final int len;
  final BigInteger[] values;
  final Callable<Integer> reference;
  final String context;
  private int address = -1;

  public Variable(String type, String name, int len, BigInteger[] values, Callable<Integer> reference, String context) {
    this.type = type;
    this.name = name;
    this.len = len;
    this.values = values;
    this.reference = reference;
    this.context = context;
  }

  public static Variable intVar(String name, BigInteger value, String context) {
    return new Variable("int", name, 1, new BigInteger[]{value}, null, context);
  }

  public static Variable pointer(String name, Callable<Integer> reference, String context) {
    return new Variable("pointer", name, 1, null, reference, context);
  }

  public static Variable string(String name, String s, String context) {
    int len = s.length();
    BigInteger[] values = new BigInteger[len + 1];
    for (int i = 0; i < len; i++) {
      values[i] = BigInteger.valueOf(s.charAt(i));
    }
    values[len] = BigInteger.ZERO;
    return new Variable("string", name,len + 1, values, null, context);
  }

  public static Variable array(String name, int len, String context) {
    BigInteger[] values = new BigInteger[len];
    for (int i = 0; i < len; i++) {
      values[i] = BigInteger.ZERO;
    }
    return new Variable("array", name, len, values, null, context);
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

  public void setAddress(int address) {
    this.address = address;
  }

  public int getAddress() {
    if (address == -1) {
      throw new RuntimeException("Can't resolve it before it has been set");
    }
    return address;
  }

  public String getName() {
    return name;
  }
}
