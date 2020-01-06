package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.Variable;

import java.math.BigInteger;

public interface ExprNode {
  ExprNode optimize();
  BigInteger value();

  void assignTo(Variable target, Assembler assembler, Assembler.Function function, String context);

  Parameter asParameter(Assembler.Function function);
}
