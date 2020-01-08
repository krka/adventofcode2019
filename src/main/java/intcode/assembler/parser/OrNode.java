package intcode.assembler.parser;

import intcode.assembler.AddOp;
import intcode.assembler.Assembler;
import intcode.assembler.Constant;
import intcode.assembler.Jump;
import intcode.assembler.Label;
import intcode.assembler.MulOp;
import intcode.assembler.Parameter;
import intcode.assembler.SetOp;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class OrNode implements ExprNode {
  private final ExprNode left;
  private final ExprNode right;

  public OrNode(ExprNode left, ExprNode right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public ExprNode optimize() {
    ExprNode left = this.left.optimize();
    ExprNode right = this.right.optimize();
    if (left.value() != null && !BigInteger.ZERO.equals(left.value())) {
      return right;
    }
    if (right.value() != null && !BigInteger.ZERO.equals(right.value())) {
      return left;
    }
    return new OrNode(left, right);
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.Function function, String context) {
    HashSet<TempVariable> tempParams = new HashSet<>();

    Label leftLabel = new Label("trueleft");
    Label doneLabel = new Label("done");

    Parameter leftParam = left.toParameter(assembler, function, tempParams);
    function.operations.add(new Jump(context, true, leftParam, null, leftLabel));
    Parameter rightParam = right.toParameter(assembler, function, tempParams);
    function.operations.add(new SetOp(context, target, rightParam));
    function.operations.add(Jump.toLabel(context, doneLabel));
    function.operations.add(leftLabel);
    function.operations.add(new SetOp(context, target, leftParam));
    function.operations.add(doneLabel);

    tempParams.forEach(TempVariable::release);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.Function function, Set<TempVariable> tempParams) {
    TempVariable target = assembler.tempSpace.getAny();
    tempParams.add(target);
    assignTo(target, assembler, function, "# todo");
    return target;
  }
}
