package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

public class Output extends Op {
  private final Parameter parameter;

  public Output(Parameter parameter) {
    this.parameter = parameter;
  }

  @Override
  public int size() {
    return 2;
  }

  @Override
  public void writeTo(List<BigInteger> res) {
    int opcode = 4 + 100 * parameter.mode().ordinal();
    res.add(BigInteger.valueOf(opcode));
    res.add(parameter.value());
  }

}
