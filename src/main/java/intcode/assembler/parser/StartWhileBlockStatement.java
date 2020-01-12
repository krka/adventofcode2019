package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Jump;
import intcode.assembler.Label;
import intcode.assembler.TempVariable;
import util.Util;

import java.math.BigInteger;
import java.util.HashSet;

public class StartWhileBlockStatement implements Statement, Block {
  private final ExprNode condition;
  private final Label startLabel = new Label("startLabel").setDefined();
  private final Label testLabel = new Label("testLabel").setDefined();
  private final Label endLabel = new Label("endLabel").setDefined();

  private final boolean loopForever;
  private final boolean doNothing;
  private final boolean isConstant;
  private int startIndex;

  public StartWhileBlockStatement(ExprNode condition) {
    this.condition = condition;
    BigInteger value = condition.value();
    isConstant = value != null;
    if (isConstant) {
      loopForever = !BigInteger.ZERO.equals(value);
      doNothing = !loopForever;
    } else {
      loopForever = false;
      doNothing = false;
    }
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    beforeLoop(assembler, caller, context);
    startIndex = caller.operations.size();
    if (!isConstant) {
      caller.operations.add(Jump.toLabel(context, testLabel));
    }
    caller.operations.add(startLabel);
    caller.pushBlock(this);
  }

  protected void beforeLoop(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
  }

  @Override
  public void finishBlock(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    if (doNothing) {
      Util.deleteFromEnd(caller.operations, startIndex);
    } else {
      if (loopForever) {
        caller.operations.add(Jump.toLabel(context, startLabel));
      } else {
        caller.operations.add(testLabel);
        HashSet<TempVariable> tempParams = new HashSet<>();
        condition.toParameter(assembler, caller, tempParams);
        JumpIfStatement jumpIfStatement = JumpIfStatement.create(condition, startLabel, null);
        jumpIfStatement.apply(assembler, caller, context);
        tempParams.forEach(TempVariable::release);
      }
    }
    caller.operations.add(endLabel);
  }

  @Override
  public boolean breakBlock(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    caller.operations.add(Jump.toLabel(context, endLabel));
    return true;
  }

}
