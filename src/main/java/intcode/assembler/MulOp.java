package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

class MulOp extends Op {
  private final Parameter first;
  private final Parameter second;
  private final Parameter target;

  public MulOp(Parameter first, Parameter second, Parameter target) {
    this.first = first;
    this.second = second;
    this.target = target;
    target.assertNotImmediate();
  }

  @Override
  public int size() {
    return 4;
  }

  @Override
  public void writeTo(List<BigInteger> res) {
    int opcode = 2 + 100 * first.mode().ordinal() + 1000 * second.mode().ordinal() + 10000 * target.mode().ordinal();
    res.add(BigInteger.valueOf(opcode));
    res.add(first.value());
    res.add(second.value());
    res.add(target.value());
  }

}
