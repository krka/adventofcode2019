package intcode.assembler.parser;

import intcode.assembler.Assembler;

public class ElseStatement implements Statement {
  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    Block block = caller.popBlock();
    if (!(block instanceof StartIfBlockStatement)) {
      throw new RuntimeException("else must follow an if-block");
    }

    ((StartIfBlockStatement) block).addElse(assembler, caller, context);
    caller.pushBlock(block);
  }
}
