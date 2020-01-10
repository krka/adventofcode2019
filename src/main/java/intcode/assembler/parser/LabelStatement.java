package intcode.assembler.parser;

import intcode.assembler.Assembler;

public class LabelStatement implements Statement {
  private final String label;

  public LabelStatement(String label) {
    this.label = label;
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    caller.addLabel(label);
  }
}
