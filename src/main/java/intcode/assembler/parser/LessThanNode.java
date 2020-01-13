package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.LessThanOp;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LessThanNode implements ExprNode {
  private final ExprNode left;
  private final ExprNode right;

  private LessThanNode(ExprNode left, ExprNode right) {
    this.left = left;
    this.right = right;
  }

  public static ExprNode create(ExprNode left, ExprNode right) {
    if (left.value() != null && right.value() != null) {
      if (left.value().compareTo(right.value()) < 0) {
        return IntConstant.ONE;
      } else {
        return IntConstant.ZERO;
      }
    }
    return new LessThanNode(left, right);
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
    function.operations.add(new LessThanOp(context, leftParam, rightParam, target));
    tempParams.forEach(TempVariable::release);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LessThanNode that = (LessThanNode) o;
    return left.equals(that.left) &&
            right.equals(that.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  @Override
  public String toString() {
    return left + " < " + right;
  }
}
