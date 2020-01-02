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
    function.add(target, a, "0", context);
  }
}
