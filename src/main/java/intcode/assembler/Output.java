package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

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
    res.addOperation(new AnnotatedOperation(context, BigInteger.valueOf(opcode), parameter.value()));
  }

}