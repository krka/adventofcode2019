package intcode.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class FunctionDefinitionInstruction extends Instruction {
  public FunctionDefinitionInstruction() {
    super(fixed("func"), space(), parameter("functionname"), maybeSpace(),
            fixed("("), maybeSpace(), commaList("parameters"), maybeSpace(), fixed(")"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String funcName = matcher.group("functionname");
    String[] parameters = matcher.group("parameters").split(",");

    if (function != assembler.main) {
      throw new RuntimeException("Can't define function inside other function: " + funcName);
    }

    List<String> params = new ArrayList<>();
    int i = 0;
    for (String parameter : parameters) {
      String param = parameter.trim();
      if (!param.isEmpty()) {
        params.add(param);
      }
    }

    function = assembler.new Function(true, funcName, context, params);
    if (assembler.functions.put(funcName, function) != null) {
      throw new RuntimeException("Function already defined: " + funcName);
    }

    assembler.setFunction(function);
  }
}
