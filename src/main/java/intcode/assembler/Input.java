package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

public class Input extends Op {
  private final Parameter variable;

  public Input(Parameter variable) {
    this.variable = variable;
    variable.assertNotImmediate();
  }

  @Override
  public int size() {
    return 2;
  }

  @Override
  public void writeTo(List<BigInteger> res) {
    int opcode = 3 + 100 * variable.mode().ordinal();
    res.add(BigInteger.valueOf(opcode));
    res.add(variable.value());
  }

}
