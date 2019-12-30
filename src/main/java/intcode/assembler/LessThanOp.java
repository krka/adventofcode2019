package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

public class LessThanOp extends Op {
  private final Parameter a;
  private final Parameter b;
  private final Parameter target;

  public LessThanOp(Parameter a, Parameter b, Parameter target) {
    this.a = a;
    this.b = b;
    this.target = target;
    target.assertNotImmediate();
  }

  @Override
  public int size() {
    return 4;
  }

  @Override
  public void writeTo(List<BigInteger> res) {
    int opcode = 7 + 100 * a.mode().ordinal() + 1000 * b.mode().ordinal() + 10000 * target.mode().ordinal();
    res.add(BigInteger.valueOf(opcode));
    res.add(a.value());
    res.add(b.value());
    res.add(target.value());
  }
}
