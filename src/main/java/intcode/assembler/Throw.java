package intcode.assembler;

public class Throw extends Op {
  private final String context;

  public Throw(String context) {
    this.context = context;
  }

  @Override
  int size() {
    return 1;
  }

  @Override
  protected void writeTo(AnnotatedIntCode res) {
    res.addOperation(new AnnotatedOperation(context, 98));
  }
}
