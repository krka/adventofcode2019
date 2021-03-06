package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Jump;
import intcode.assembler.Label;
import intcode.assembler.Parameter;
import intcode.assembler.SetOp;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AndNode implements ExprNode {
  private final ExprNode left;
  private final ExprNode right;

  private AndNode(ExprNode left, ExprNode right) {
    this.left = left;
    this.right = right;
  }

  public static ExprNode create(ExprNode left, ExprNode right) {
    if (BigInteger.ZERO.equals(left.value())) {
      return IntConstant.ZERO;
    }
    if (BigInteger.ZERO.equals(right.value())) {
      return IntConstant.ZERO;
    }
    if (left.value() != null) {
      // left can't be zero, so we must return right
      return right;
    }
    return new AndNode(left, right);
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    HashSet<TempVariable> tempParams = new HashSet<>();

    Label leftLabel = new Label("trueleft").setDefined();
    Label doneLabel = new Label("done").setDefined();

    Parameter leftParam = left.toParameter(assembler, function, tempParams);
    function.operations.add(new Jump(context + " (jump if left)", true, leftParam, null, leftLabel));
    function.operations.add(new SetOp(context + " (set to left)", leftParam, target));
    function.operations.add(Jump.toLabel(context + " (jump to done)", doneLabel));
    function.operations.add(leftLabel);
    Parameter rightParam = right.toParameter(assembler, function, tempParams);
    function.operations.add(new SetOp(context + " (set to right)", rightParam, target));
    function.operations.add(doneLabel);

    tempParams.forEach(TempVariable::release);
  }

  @Override
  public String toString() {
    return left + " && " + right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AndNode andNode = (AndNode) o;
    return left.equals(andNode.left) &&
            right.equals(andNode.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

}
