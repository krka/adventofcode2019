package intcode.assembler.parser;

public class IndexNode {
  private final ExprNode index;

  public IndexNode(ExprNode index) {
    this.index = index;
  }

  public ExprNode getIndex() {
    return index;
  }
}
