package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.EqOp;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EqNode implements ExprNode {
  private final ExprNode left;
  private final ExprNode right;

  private EqNode(ExprNode left, ExprNode right) {
    this.left = left;
    this.right = right;
  }

  public static ExprNode create(ExprNode left, ExprNode right) {
    if (left.value() != null && right.value() != null) {
      if (left.value().equals(right.value())) {
        return IntConstant.ONE;
      } else {
        return IntConstant.ZERO;
      }
    }
    return new EqNode(left, right);
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    HashSet<TempVariable> tempParams = new HashSet<>();
    Parameter leftParam = left.toParameter(assembler, function, tempParams);
    Parameter rightParam = right.toParameter(assembler, function, tempParams);
    function.operations.add(new EqOp(context, leftParam, rightParam, target));
    tempParams.forEach(TempVariable::release);
  }

  @Override
  public String toString() {
    return left + " == " + right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EqNode eqNode = (EqNode) o;
    return left.equals(eqNode.left) &&
            right.equals(eqNode.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }
}
