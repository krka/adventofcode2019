package intcode.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ReturnInstruction extends Instruction {
  public ReturnInstruction() {
    super(Token.fixed("return"), Token.maybeSpace(), Token.commaList("returnvalues"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    if (function == assembler.main) {
      throw new RuntimeException("Can not return from main");
    }

    String[] parameters = matcher.group("returnvalues").split(",");

    List<Parameter> returnValues = new ArrayList<>();
    for (String parameter : parameters) {
      String name = parameter.trim();
      if (!name.isEmpty()) {
        returnValues.add(function.resolveParameter(name));
      }
    }

    assembler.ensureTempSpaceSize(returnValues.size());
    function.operations.add(new Return(function, returnValues, assembler.tempSpace));
  }
}
