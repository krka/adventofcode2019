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

  public AddNode(ExprNode left, ExprNode right) {
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

  @Override
  public ExprNode optimize() {
    ExprNode newLeft = left.optimize();
    ExprNode newRight = right.optimize();
    if (BigInteger.ZERO.equals(newLeft.value())) {
      return newRight;
    }
    if (BigInteger.ZERO.equals(newRight.value())) {
      return newLeft;
    }
    if (newLeft.value() != null && newRight.value() != null) {
      return new IntConstant(newLeft.value().add(newRight.value()));
    }
    return new AddNode(newLeft, newRight);
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.Function function, String context) {
    Set<TempVariable> tempParams = new HashSet<>();
    Parameter leftParam = left.toParameter(assembler, function, tempParams);
    Parameter rightParam = right.toParameter(assembler, function, tempParams);

    function.operations.add(new AddOp(context, leftParam, rightParam, target));

    tempParams.forEach(TempVariable::release);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.Function function, Set<TempVariable> tempParams) {
    TempVariable target = assembler.tempSpace.getAny();
    tempParams.add(target);
    assignTo(target, assembler, function, "# " + target + " = " + toString());
    return target;
  }
}
