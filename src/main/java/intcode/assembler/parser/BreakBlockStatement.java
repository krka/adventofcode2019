package intcode.assembler.parser;

import intcode.assembler.Assembler;

public class BreakBlockStatement implements Statement {
  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    caller.breakOutOfBlock(assembler, caller, context);
  }
}
