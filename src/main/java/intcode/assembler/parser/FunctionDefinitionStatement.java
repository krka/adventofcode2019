package intcode.assembler.parser;

import intcode.assembler.Assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FunctionDefinitionStatement implements Statement {

  private final String funcName;
  private final ExpressionList parameters;

  public FunctionDefinitionStatement(String funcName, ExpressionList parameters) {
    this.funcName = funcName;
    this.parameters = Objects.requireNonNullElse(parameters, ExpressionList.empty());
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    if (caller != assembler.main) {
      throw new RuntimeException("Can't define function inside other function: " + funcName);
    }

    List<String> params = new ArrayList<>();
    int i = 0;
    for (ExprNode parameter : parameters.expressions) {
      if (parameter instanceof VarNode) {
        params.add(((VarNode) parameter).getName());
      } else {
        throw new RuntimeException("Not a variable: " + parameter);
      }
    }

    Assembler.IntCodeFunction function = assembler.new IntCodeFunction(true, funcName, context, params);
    if (assembler.functions.put(funcName, function) != null) {
      throw new RuntimeException("Function already defined: " + funcName);
    }
    assembler.setFunction(function);
  }
}
