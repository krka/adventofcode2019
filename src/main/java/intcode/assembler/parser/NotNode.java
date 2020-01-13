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

  private NotNode(ExprNode child) {
    this.child = child;
  }

  public static ExprNode create(ExprNode child) {
    if (child instanceof IntConstant) {
      IntConstant intChild = (IntConstant) child;
      if (intChild.value().equals(BigInteger.ZERO)) {
        return IntConstant.ONE;
      } else {
        return IntConstant.ZERO;
      }
    }

    if (child instanceof NotNode) {
      ExprNode grandChild = ((NotNode) child).child;
      if (grandChild instanceof NotNode) {
        return grandChild;
      }
    }
    return new NotNode(child);

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
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    Set<TempVariable> tempParams = new HashSet<>();
    Parameter parameter = child.toParameter(assembler, function, tempParams);
    function.operations.add(new EqOp(context, parameter, Constant.ZERO, target));
    tempParams.forEach(TempVariable::release);
  }
}
