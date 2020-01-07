package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.LessThanOp;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class GreaterThanNode implements ExprNode {
  private final ExprNode left;
  private final ExprNode right;

  public GreaterThanNode(ExprNode left, ExprNode right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public ExprNode optimize() {
    ExprNode left = this.left.optimize();
    ExprNode right = this.right.optimize();
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
  public void assignTo(Variable target, Assembler assembler, Assembler.Function function, String context) {
    HashSet<TempVariable> tempParams = new HashSet<>();
    Parameter leftParam = left.toParameter(assembler, function, tempParams);
    Parameter rightParam = right.toParameter(assembler, function, tempParams);
    function.operations.add(new LessThanOp(context, rightParam, leftParam, target));
    tempParams.forEach(TempVariable::release);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.Function function, Set<TempVariable> tempParams) {
    TempVariable target = assembler.tempSpace.getAny();
    Parameter leftParam = left.toParameter(assembler, function, tempParams);
    Parameter rightParam = right.toParameter(assembler, function, tempParams);
    function.operations.add(new LessThanOp(" todo", rightParam, leftParam, target));
    return target;
  }
}
