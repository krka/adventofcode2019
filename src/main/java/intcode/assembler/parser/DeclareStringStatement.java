package intcode.assembler.parser;

import intcode.assembler.Assembler;

public class DeclareStringStatement implements Statement {
  private final String name;
  private final String value;

  public DeclareStringStatement(String name, String value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    caller.declareString(name, value, context);
  }
}
