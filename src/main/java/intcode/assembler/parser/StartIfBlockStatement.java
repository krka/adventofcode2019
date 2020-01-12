package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Jump;
import intcode.assembler.Label;
import intcode.assembler.TempVariable;

import java.util.HashSet;

public class StartIfBlockStatement implements Statement, Block {
  private final ExprNode condition;
  private final Label elseblock;
  private final Label endblock;
  private boolean hasElse;

  public StartIfBlockStatement(ExprNode condition) {
    this.condition = condition;
    this.elseblock = new Label("elseBlock").setDefined();
    this.endblock = new Label("endBlock").setDefined();
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {

    HashSet<TempVariable> tempParams = new HashSet<>();
    condition.toParameter(assembler, caller, tempParams);

    JumpIfStatement jumpIfStatement = JumpIfStatement.create(NotNode.create(condition), elseblock, null);
    jumpIfStatement.apply(assembler, caller, context);
    tempParams.forEach(TempVariable::release);
    caller.pushBlock(this);
  }

  public void addElse(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    hasElse = true;
    caller.operations.add(Jump.toLabel(context, endblock));
    caller.operations.add(elseblock);
  }

  @Override
  public void finishBlock(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    if (!hasElse) {
      caller.operations.add(elseblock);
    } else {
      caller.operations.add(endblock);
    }
  }

}
