package intcode.assembler.parser;

import intcode.assembler.Assembler;

public class IncludeResourceStatement implements Statement {
  private String filename;

  public IncludeResourceStatement(String filename) {
    this.filename = filename;
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    assembler.includeResource(filename);
  }
}
