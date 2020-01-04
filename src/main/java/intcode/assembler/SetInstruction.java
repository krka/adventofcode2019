package intcode.assembler;

import java.util.regex.Matcher;

public class SetInstruction extends Instruction {
  public SetInstruction() {
    super(parameter("name"), fixed("="), parameter("a"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String target = matcher.group("name");
    String a = matcher.group("a");
    function.operations.add(new AddOp(context, function.resolveParameter(a), function.resolveParameter("0"), function.resolveParameter(target)));
  }
}
