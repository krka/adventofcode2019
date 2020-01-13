package intcode.assembler.parser;

import intcode.assembler.AddOp;
import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class AddNode implements ExprNode {
  private final ExprNode left;
  private final ExprNode right;

  private AddNode(ExprNode left, ExprNode right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public String toString() {
    return left + " + " + right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AddNode addNode = (AddNode) o;
    return left.equals(addNode.left) &&
            right.equals(addNode.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  public static ExprNode create(ExprNode left, ExprNode right) {
    if (BigInteger.ZERO.equals(left.value())) {
      return right;
    }
    if (BigInteger.ZERO.equals(right.value())) {
      return left;
    }
    if (left.value() != null && right.value() != null) {
      return new IntConstant(left.value().add(right.value()));
    }
    return new AddNode(left, right);
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

    function.operations.add(new AddOp(context, leftParam, rightParam, target));

    tempParams.forEach(TempVariable::release);
  }
}
