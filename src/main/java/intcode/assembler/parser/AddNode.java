package intcode.assembler.parser;

import intcode.assembler.AddOp;
import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.Objects;

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
    Variable leftTemp = null;
    Variable rightTemp = null;
    Parameter leftParam = left.asParameter(function);
    if (leftParam == null) {
      leftTemp = assembler.tempSpace.getAny();
      left.assignTo(leftTemp, assembler, function, "# " + leftTemp + " = " + left.toString());
      leftParam = leftTemp;
    }
    Parameter rightParam = right.asParameter(function);
    if (rightParam == null) {
      rightTemp = assembler.tempSpace.getAny();
      right.assignTo(rightTemp, assembler, function, "# " + rightTemp + " = " + right.toString());
      rightParam = rightTemp;
    }

    function.operations.add(new AddOp(context, leftParam, rightParam, target));

    assembler.tempSpace.release(leftTemp);
    assembler.tempSpace.release(rightTemp);
  }

  @Override
  public Parameter asParameter(Assembler.Function function) {
    return null;
  }

}
