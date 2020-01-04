package intcode.assembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

public class NoReturnFunctionCallInstruction extends Instruction {
  public NoReturnFunctionCallInstruction() {
    super(parameter("functionname"), maybeSpace(), fixed("("),
            maybeSpace(), commaList("parameters"), maybeSpace(), fixed(")"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String funcName = matcher.group("functionname");
    String[] parameters = matcher.group("parameters").split(",");

    List<Parameter> parameters2 = new ArrayList<>();
    for (String parameter : parameters) {
      String param = parameter.trim();
      if (!param.isEmpty()) {
        parameters2.add(function.resolveParameter(param));
      }
    }

    function.addFunctionCall(funcName, parameters2, Collections.emptyList(), context);
  }
}
