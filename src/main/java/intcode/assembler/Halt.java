package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

public class Halt extends Op {
  @Override
  public int size() {
    return 1;
  }

  @Override
  public void writeTo(List<BigInteger> res) {
    res.add(BigInteger.valueOf(99));
  }
}
