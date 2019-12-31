package intcode.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class FunctionCallInstruction extends Instruction {
  public FunctionCallInstruction() {
    super(Token.commaList("returnvalues"), Token.maybeSpace(), Token.fixed("="), Token.maybeSpace(), Token.variable("functionname"), Token.maybeSpace(), Token.fixed("("),
            Token.maybeSpace(), Token.commaList("parameters"), Token.maybeSpace(), Token.fixed(")"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String funcName = matcher.group("functionname");
    String[] parameters = matcher.group("parameters").split(",");
    String[] returnValues = matcher.group("returnvalues").split(",");

    List<Parameter> parameters2 = new ArrayList<>();
    for (String parameter : parameters) {
      parameters2.add(function.resolveParameter(parameter.trim()));
    }

    List<Variable> returnValues2 = new ArrayList<>();
    for (String returnvalue : returnValues) {
      returnValues2.add(function.resolveVariable(returnvalue.trim()));
    }

    function.operations.add(new FunctionCall(assembler, funcName, parameters2, returnValues2));

  }
}
