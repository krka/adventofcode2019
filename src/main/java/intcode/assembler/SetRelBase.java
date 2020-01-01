package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

public class SetRelBase extends Op {
  static final int SIZE = 2;
  private int parameter;
  private final String context;

  public SetRelBase(String context) {
    this.context = context;
  }

  public SetRelBase setParameter(int parameter) {
    this.parameter = parameter;
    return this;
  }

  @Override
  public int size() {
    return SIZE;
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
    int ordinal = ParameterMode.IMMEDIATE.ordinal();
    int val = 9 + 100 * ordinal;
    res.addOperation(new AnnotatedOperation(context, val, parameter));
  }
}
