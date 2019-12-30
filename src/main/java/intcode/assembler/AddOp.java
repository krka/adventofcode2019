package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

class AddOp extends Op {
  static final int SIZE = 4;
  private final Parameter first;
  private final Parameter second;
  private Parameter target;


  public AddOp(Parameter first, Parameter second, Parameter target) {
    this.first = first;
    this.second = second;
    this.target = target;
    target.assertNotImmediate();
  }

  public void setTarget(Parameter target) {
    this.target = target;
    target.assertNotImmediate();
  }

  @Override
  public int size() {
    return SIZE;
  }

  @Override
  public void writeTo(List<BigInteger> res) {
    int opcode = 1 + 100 * first.mode().ordinal() + 1000 * second.mode().ordinal() + 10000 * target.mode().ordinal();
    res.add(BigInteger.valueOf(opcode));
    res.add(first.value());
    res.add(second.value());
    res.add(target.value());
  }
}
