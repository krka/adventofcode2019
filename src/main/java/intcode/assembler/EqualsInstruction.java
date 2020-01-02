package intcode.assembler;

import java.util.regex.Matcher;

public class EqualsInstruction extends Instruction {
  public EqualsInstruction() {
    super(parameter("name"), fixed("="), parameter("a"), fixed("=="), parameter("b"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String target = matcher.group("name");
    String a = matcher.group("a");
    String b = matcher.group("b");
    function.eq(target, a, b, context);
  }
}
