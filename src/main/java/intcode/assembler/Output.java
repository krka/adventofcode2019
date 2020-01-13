package intcode.assembler;

import java.math.BigInteger;

public class Output extends Op {
  private final Parameter parameter;
  private final String context;

  public Output(Parameter parameter, String context) {
    this.parameter = parameter;
    this.context = context;
  }

  @Override
  public int size() {
    return 2;
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
    int opcode = 4 + 100 * parameter.mode().ordinal();
    res.addOperation(new AnnotatedOperation(toString(), context, BigInteger.valueOf(opcode), parameter.value()));
  }

  @Override
  public String toString() {
    return "OUTPUT <- " + parameter;
  }
}
