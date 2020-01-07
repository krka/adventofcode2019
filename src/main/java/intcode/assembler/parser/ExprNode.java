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

  void assignTo(Variable target, Assembler assembler, Assembler.Function function, String context);

  Parameter toParameter(Assembler assembler, Assembler.Function function, Set<TempVariable> tempParams);

}
