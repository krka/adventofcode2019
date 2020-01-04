package intcode.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class FunctionCallInstruction extends Instruction {
  public FunctionCallInstruction() {
    super(commaList("returnvalues"), maybeSpace(), fixed("="), maybeSpace(), parameter("functionname"), maybeSpace(), fixed("("),
            maybeSpace(), commaList("parameters"), maybeSpace(), fixed(")"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String funcName = matcher.group("functionname");
    String[] parameters = matcher.group("parameters").split(",");
    String[] returnValues = matcher.group("returnvalues").split(",");

    List<Parameter> parameters2 = new ArrayList<>();
    for (String parameter : parameters) {
      String param = parameter.trim();
      if (!param.isEmpty()) {
        parameters2.add(function.resolveParameter(param));
      }
    }

    List<Variable> returnValues2 = new ArrayList<>();
    for (String returnvalue : returnValues) {
      returnValues2.add(function.resolveVariable(returnvalue.trim()));
    }

    function.addFunctionCall(funcName, parameters2, returnValues2, context);
  }
}
