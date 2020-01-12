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

public class OrNode implements ExprNode {
  private final ExprNode left;
  private final ExprNode right;

  private OrNode(ExprNode left, ExprNode right) {
    this.left = left;
    this.right = right;
  }

  public static ExprNode create(ExprNode left, ExprNode right) {
    if (left.value() != null) {
      if (!BigInteger.ZERO.equals(left.value())) {
        return left;
      }
      if (right.value() != null && !BigInteger.ZERO.equals(right.value())) {
        return right;
      }
    }
    return new OrNode(left, right);
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
    function.operations.add(new Jump(context, true, leftParam, null, leftLabel));
    Parameter rightParam = right.toParameter(assembler, function, tempParams);
    function.operations.add(new SetOp(context, rightParam, target));
    function.operations.add(Jump.toLabel(context, doneLabel));
    function.operations.add(leftLabel);
    function.operations.add(new SetOp(context, leftParam, target));
    function.operations.add(doneLabel);

    tempParams.forEach(TempVariable::release);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.IntCodeFunction function, Set<TempVariable> tempParams) {
    TempVariable target = assembler.tempSpace.getAny();
    tempParams.add(target);
    assignTo(target, assembler, function, "# " + target + " = " + toString());
    return target;
  }

  @Override
  public String toString() {
    return left + " || " + right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrNode orNode = (OrNode) o;
    return left.equals(orNode.left) &&
            right.equals(orNode.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }
}
