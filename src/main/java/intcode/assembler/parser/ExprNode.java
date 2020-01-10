package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.Set;

public interface ExprNode {
  ExprNode optimize();
  BigInteger value();

  void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context);

  Parameter toParameter(Assembler assembler, Assembler.IntCodeFunction function, Set<TempVariable> tempParams);

  default void assignValue(Assembler assembler, Assembler.IntCodeFunction function, String context, ExprNode expr) {
    throw new RuntimeException("Can't assign a value to " + this.getClass().getSimpleName());
  }

  default ExpressionList toExpressionList() {
    return new ExpressionList(this);
  }
}
