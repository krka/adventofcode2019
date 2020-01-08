package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Constant;
import intcode.assembler.EqOp;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NotNode implements ExprNode {
  private final ExprNode child;

  public NotNode(ExprNode child) {
    this.child = child;
  }

  public ExprNode getChild() {
    return child;
  }

  @Override
  public String toString() {
    return "!" + child;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NotNode negNode = (NotNode) o;
    return child.equals(negNode.child);
  }

  @Override
  public int hashCode() {
    return Objects.hash(child);
  }

  @Override
  public ExprNode optimize() {
    ExprNode newChild = child.optimize();
    if (newChild instanceof IntConstant) {
      IntConstant child2 = (IntConstant) child;
      if (child2.value().equals(BigInteger.ZERO)) {
        return IntConstant.ONE;
      } else {
        return IntConstant.ZERO;
      }
    }
    return new NotNode(newChild);
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.Function function, String context) {
    Set<TempVariable> tempParams = new HashSet<>();
    Parameter parameter = child.toParameter(assembler, function, tempParams);
    function.operations.add(new EqOp(context, parameter, Constant.ZERO, target));
    tempParams.forEach(TempVariable::release);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.Function function, Set<TempVariable> tempParams) {
    TempVariable target = assembler.tempSpace.getAny();
    tempParams.add(target);
    assignTo(target, assembler, function, "# todo");
    return target;
  }
}
