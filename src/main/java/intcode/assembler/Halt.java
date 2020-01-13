package intcode.assembler;

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
    res.addOperation(new AnnotatedOperation(toString(), context, 99));
  }

  @Override
  public String toString() {
    return "HALT";
  }
}
