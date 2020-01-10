package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;

import java.util.HashSet;

public class DeclareArrayStatement implements Statement {
  private final ExprNode size;
  private final String name;

  public DeclareArrayStatement(ExprNode size, String name) {
    this.size = size;
    this.name = name;
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    HashSet<TempVariable> tempVariables = new HashSet<>();
    Parameter parameter = size.toParameter(assembler, caller, tempVariables);
    caller.declareArray(name, parameter, context);
    tempVariables.forEach(TempVariable::release);
  }
}
