package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

public class Jump extends Op {
  static final int SIZE = 3;
  private final boolean isTrue;
  private final Parameter parameter;
  private final Parameter target;
  private final Label label;

  public Jump(boolean isTrue, Parameter parameter, Parameter target, Label label) {
    this.isTrue = isTrue;
    this.parameter = parameter;
    this.target = target;
    this.label = label;
  }

  @Override
  public int size() {
    return SIZE;
  }

  @Override
  public void writeTo(List<BigInteger> res) {
    int opcode = isTrue ? 5 : 6;
    Parameter targetAddress = target == null ? label.getLabelAddress() : target;
    opcode += 100 * parameter.mode().ordinal() + 1000 * targetAddress.mode().ordinal();
    res.add(BigInteger.valueOf(opcode));
    res.add(parameter.value());
    res.add(targetAddress.value());
  }

}
