package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.SetOp;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;

class VarNode implements ExprNode {
  private final String name;

  VarNode(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VarNode varNode = (VarNode) o;
    return name.equals(varNode.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
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
    Variable variable = function.resolveVariable(name);
    function.operations.add(new SetOp(context, variable, target));
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.IntCodeFunction function, Set<TempVariable> tempParams) {
    return function.resolveVariable(name);
  }

  @Override
  public void assignValue(Assembler assembler, Assembler.IntCodeFunction function, String context, ExprNode expr) {
    Variable variable = function.resolveVariable(name);
    expr.assignTo(variable, assembler, function, context);
  }

  public String getName() {
    return name;
  }
}
