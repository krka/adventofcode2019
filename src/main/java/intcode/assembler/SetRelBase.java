package intcode.assembler;

import java.math.BigInteger;

public class SetRelBase extends Op {
  static final int SIZE = 2;
  private Parameter parameter;
  private final String context;

  public SetRelBase(String context) {
    this.context = context;
  }

  public SetRelBase setParameter(Parameter parameter) {
    this.parameter = parameter;
    return this;
  }

  @Override
  public int size() {
    return SIZE;
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
    int op = 9 + 100 * parameter.mode().ordinal();
    res.addOperation(new AnnotatedOperation(context, BigInteger.valueOf(op), parameter.value()));
  }
}
