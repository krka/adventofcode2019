package intcode.assembler;

import java.util.regex.Matcher;

public class LessThanInstruction extends Instruction {
  public LessThanInstruction() {
    super(parameter("name"), maybeSpace(), fixed("="), maybeSpace(),
            parameter("a"), maybeSpace(),
            fixed("<"), maybeSpace(),
            parameter("b"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String target = matcher.group("name");
    String a = matcher.group("a");
    String b = matcher.group("b");
    function.lessThan(target, a, b, context);
  }
}
