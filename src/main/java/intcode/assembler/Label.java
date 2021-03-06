package intcode.assembler;

public class Label extends Op {
  private final String label;
  private boolean defined;

  public Label(String label) {
    this.label = label;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
    res.nextHasLabel(label);
  }

  public Label setDefined() {
    if (defined) {
      throw new RuntimeException("Label " + label + " already defined");
    }
    defined = true;
    return this;
  }

  public Parameter getLabelAddress() {
    if (!defined) {
      throw new RuntimeException("Label " + label + " is not defined");
    }
    return Constant.of(getAddress());
  }

  @Override
  public String toString() {
    return label;
  }
}
