package intcode.assembler;

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

    function = assembler.new Function(true, funcName, context);
    if (assembler.functions.put(funcName, function) != null) {
      throw new RuntimeException("Function already defined: " + funcName);
    }

    int i = 0;
    for (String parameter : parameters) {
      String param = parameter.trim();
      if (!param.isEmpty()) {
        StackVariable variable = function.addStackVariable(param);
        Variable inParam = assembler.getParam(i);
        function.operations.add(new AddOp("# copy param " + i, inParam, Constant.ZERO, variable));
        i++;
      }
    }

    assembler.setFunction(function);
  }
}
