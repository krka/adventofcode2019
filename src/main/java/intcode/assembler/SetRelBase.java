package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

public class SetRelBase extends Op {
  static final int SIZE = 2;
  private int parameter;

  public SetRelBase setParameter(int parameter) {
    this.parameter = parameter;
    return this;
  }

  @Override
  public int size() {
    return SIZE;
  }

  @Override
  public void writeTo(List<BigInteger> res) {
    int ordinal = ParameterMode.IMMEDIATE.ordinal();
    int val = 9 + 100 * ordinal;
    res.add(BigInteger.valueOf(val));
    res.add(BigInteger.valueOf(parameter));

  }
}
