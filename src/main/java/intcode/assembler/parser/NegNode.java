package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Constant;
import intcode.assembler.MulOp;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NegNode implements ExprNode {
  private final ExprNode child;

  public NegNode(ExprNode child) {
    this.child = child;
  }

  @Override
  public String toString() {
    return "-" + child;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NegNode negNode = (NegNode) o;
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
      return new IntConstant(child2.value().negate());
    }
    return new NegNode(newChild);
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    Set<TempVariable> tempParams = new HashSet<>();
    Parameter parameter = child.toParameter(assembler, function, tempParams);
    function.operations.add(new MulOp(context, parameter, Constant.MINUS_ONE, target));
    tempParams.forEach(TempVariable::release);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.IntCodeFunction function, Set<TempVariable> tempParams) {
    TempVariable target = assembler.tempSpace.getAny();
    tempParams.add(target);
    Parameter parameter = child.toParameter(assembler, function, tempParams);
    function.operations.add(new MulOp("todo", parameter, Constant.MINUS_ONE, target));
    return target;
  }

}
