package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Jump;
import intcode.assembler.Label;
import intcode.assembler.TempVariable;
import util.Util;

import java.math.BigInteger;
import java.util.HashSet;

public class StartIfBlockStatement implements Statement, Block {
  private final ExprNode condition;
  private final Label elseblock;
  private final Label endblock;
  private final boolean alwaysRun;
  private final boolean neverRun;
  private boolean hasElse;
  private final boolean isConstant;
  private int startIndex;

  public StartIfBlockStatement(ExprNode condition) {
    this.condition = condition;
    this.elseblock = new Label("elseBlock").setDefined();
    this.endblock = new Label("endBlock").setDefined();
    BigInteger value = condition.value();
    isConstant = value != null;
    if (isConstant) {
      alwaysRun = !BigInteger.ZERO.equals(value);
      neverRun = !alwaysRun;
    } else {
      alwaysRun = false;
      neverRun = false;
    }
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    startIndex = caller.operations.size();
    if (!isConstant) {
      JumpIfStatement jumpIfStatement = JumpIfStatement.create(NotNode.create(condition), elseblock, null);
      jumpIfStatement.apply(assembler, caller, context);
    }
    caller.pushBlock(this);
  }

  public void addElse(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    hasElse = true;
    if (isConstant) {
      if (neverRun) {
        Util.deleteFromEnd(caller.operations, startIndex);
      } else {
        startIndex = caller.operations.size();
      }
    } else {
      caller.operations.add(Jump.toLabel(context, endblock));
      caller.operations.add(elseblock);
    }
  }

  @Override
  public void finishBlock(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    if (isConstant) {
      if (alwaysRun) {
        if (hasElse) {
          Util.deleteFromEnd(caller.operations, startIndex);
        }
      }
    } else {
      if (!hasElse) {
        caller.operations.add(elseblock);
      } else {
        caller.operations.add(endblock);
      }
    }
  }

  @Override
  public boolean breakBlock(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    return false;
  }

}
