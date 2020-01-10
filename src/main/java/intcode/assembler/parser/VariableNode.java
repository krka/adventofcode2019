package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.SetOp;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.Set;

class VariableNode implements ExprNode {
  private final Variable source;

  public VariableNode(Variable source) {
    this.source = source;
  }

  @Override
  public ExprNode optimize() {
    return this;
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    function.operations.add(new SetOp(context, source, target));
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.IntCodeFunction function, Set<TempVariable> tempParams) {
    return source;
  }

  @Override
  public void assignValue(Assembler assembler, Assembler.IntCodeFunction function, String context, ExprNode expr) {
    expr.assignTo(source, assembler, function, context);
  }

}
