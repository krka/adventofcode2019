package intcode.assembler.parser;

import intcode.assembler.AddOp;
import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.SetOp;
import intcode.assembler.StackVariable;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.util.HashSet;
import java.util.Objects;

public class StartForLoopBlockStatement extends StartWhileBlockStatement {
  private final VarNode identifier;
  private final ExprNode start;
  private final ExprNode step;

  private Variable variable;

  public StartForLoopBlockStatement(VarNode identifier, ExprNode start, ExprNode condition, ExprNode step) {
    super(condition);
    this.identifier = identifier;
    this.start = start;
    this.step = Objects.requireNonNullElse(step, IntConstant.ONE);
  }

  @Override
  protected void beforeLoop(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    variable = caller.resolveVariableOpt(identifier.getName());
    if (variable == null) {
      variable = caller.addStackVariable(identifier.getName());
    }
    HashSet<TempVariable> tempParams = new HashSet<>();
    Parameter startValue = start.toParameter(assembler, caller, tempParams);
    caller.operations.add(new SetOp(context, startValue, variable));
    tempParams.forEach(TempVariable::release);
  }

  @Override
  public void finishBlock(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    HashSet<TempVariable> tempParams = new HashSet<>();
    Parameter stepValue = step.toParameter(assembler, caller, tempParams);
    caller.operations.add(new AddOp(context, variable, stepValue, variable));
    tempParams.forEach(TempVariable::release);

    super.finishBlock(assembler, caller, context);
  }
}
