package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

public class Halt extends Op {

  private final String context;

  public Halt(String context) {
    this.context = context;
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
    res.addOperation(new AnnotatedOperation(context, 99));
  }
}
