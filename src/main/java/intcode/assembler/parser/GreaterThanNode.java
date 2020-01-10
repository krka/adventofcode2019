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

public class GreaterThanNode implements ExprNode {
  private final ExprNode left;
  private final ExprNode right;

  private GreaterThanNode(ExprNode left, ExprNode right) {
    this.left = left;
    this.right = right;
  }

  public static ExprNode create(ExprNode left, ExprNode right) {
    if (left.value() != null && right.value() != null) {
      if (left.value().compareTo(right.value()) > 0) {
        return IntConstant.ONE;
      } else {
        return IntConstant.ZERO;
      }
    }
    return new GreaterThanNode(left, right);
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
    function.operations.add(new LessThanOp(context, rightParam, leftParam, target));
    tempParams.forEach(TempVariable::release);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.IntCodeFunction function, Set<TempVariable> tempParams) {
    TempVariable target = assembler.tempSpace.getAny();
    tempParams.add(target);
    Parameter leftParam = left.toParameter(assembler, function, tempParams);
    Parameter rightParam = right.toParameter(assembler, function, tempParams);
    function.operations.add(new LessThanOp("# " + target + " = " + toString() , rightParam, leftParam, target));
    return target;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GreaterThanNode that = (GreaterThanNode) o;
    return left.equals(that.left) &&
            right.equals(that.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  @Override
  public String toString() {
    return left + " > " + right;
  }
}
