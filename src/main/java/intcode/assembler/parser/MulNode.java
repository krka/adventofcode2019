package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.MulOp;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class MulNode implements ExprNode {
  private final ExprNode left;
  private final ExprNode right;

  private MulNode(ExprNode left, ExprNode right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public String toString() {
    return left + " * " + right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MulNode mulNode = (MulNode) o;
    return left.equals(mulNode.left) &&
            right.equals(mulNode.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  public static ExprNode create(ExprNode left, ExprNode right) {
    if (BigInteger.ZERO.equals(left.value())) {
      return IntConstant.ZERO;
    }
    if (BigInteger.ZERO.equals(right.value())) {
      return IntConstant.ZERO;
    }
    if (BigInteger.ONE.equals(left.value())) {
      return right;
    }
    if (BigInteger.ONE.equals(right.value())) {
      return left;
    }
    if (left.value() != null && right.value() != null) {
      return new IntConstant(left.value().multiply(right.value()));
    }
    return new MulNode(left, right);
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    Set<TempVariable> tempParams = new HashSet<>();
    Parameter leftParam = left.toParameter(assembler, function, tempParams);
    Parameter rightParam = right.toParameter(assembler, function, tempParams);

    function.operations.add(new MulOp(context, leftParam, rightParam, target));

    tempParams.forEach(TempVariable::release);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.IntCodeFunction function, Set<TempVariable> tempParams) {
    Parameter leftParam = left.toParameter(assembler, function, tempParams);
    Parameter rightParam = right.toParameter(assembler, function, tempParams);

    TempVariable target = assembler.tempSpace.getAny();
    tempParams.add(target);
    function.operations.add(new MulOp("# " + target + " = " + toString(), leftParam, rightParam, target));
    return target;
  }

}
