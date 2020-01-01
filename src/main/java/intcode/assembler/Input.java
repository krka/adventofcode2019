package intcode.assembler;

import java.math.BigInteger;

public class Input extends Op {
  private final Parameter variable;
  private final String context;

  public Input(Parameter variable, String context) {
    this.variable = variable;
    this.context = context;
    variable.assertNotImmediate();
  }

  @Override
  public int size() {
    return 2;
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
    int opcode = 3 + 100 * variable.mode().ordinal();
    res.addOperation(new AnnotatedOperation(context, BigInteger.valueOf(opcode), variable.value()));
  }

}
