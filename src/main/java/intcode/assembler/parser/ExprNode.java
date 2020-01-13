package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.Set;

public interface ExprNode {
  BigInteger value();

  void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context);

  default Parameter toParameter(Assembler assembler, Assembler.IntCodeFunction function, Set<TempVariable> tempParams) {
    TempVariable target = assembler.tempSpace.getAny();
    tempParams.add(target);
    assignTo(target, assembler, function, "# " + target + " = " + toString());
    return target;
  }

  default void assignValue(Assembler assembler, Assembler.IntCodeFunction function, String context, ExprNode expr) {
    throw new RuntimeException("Can't assign a value to " + this.getClass().getSimpleName());
  }

  default ExpressionList toExpressionList() {
    return new ExpressionList(this);
  }
}
