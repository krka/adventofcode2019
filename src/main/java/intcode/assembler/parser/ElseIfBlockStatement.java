package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Jump;
import intcode.assembler.Label;
import intcode.assembler.TempVariable;
import util.Util;

import java.math.BigInteger;
import java.util.HashSet;

public class ElseIfBlockStatement extends StartIfBlockStatement {

  private StartIfBlockStatement parent;

  public ElseIfBlockStatement(ExprNode condition) {
    super(condition);
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    Block block = caller.popBlock();
    if (!(block instanceof StartIfBlockStatement)) {
      throw new RuntimeException("elseif must follow an if-statement");
    }
    parent = (StartIfBlockStatement) block;
    parent.addElse(assembler, caller, context);

    super.apply(assembler, caller, context);
  }

  @Override
  public void finishBlock(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    super.finishBlock(assembler, caller, context);
    parent.finishBlock(assembler, caller, context);
  }
}
