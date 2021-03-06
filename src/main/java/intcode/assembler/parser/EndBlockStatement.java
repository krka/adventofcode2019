package intcode.assembler.parser;

import intcode.assembler.Assembler;

public class EndBlockStatement implements Statement {
  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    if (caller.hasBlocks()) {
      Block block = caller.peekBlock();
      block.finishBlock(assembler, caller, context);
      caller.popBlock();
    } else {
      if (caller == assembler.main) {
        throw new RuntimeException("Can not return from main");
      }
      caller.endFunc();
      assembler.setFunction(assembler.main);
    }
  }
}
