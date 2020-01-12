package intcode.assembler.parser;

import intcode.assembler.Assembler;

public class EndIfStatement implements Statement {
  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    Block block = caller.popBlock();
    block.finishBlock(assembler, caller, context);

  }
}
