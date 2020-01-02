package intcode.assembler;

import java.util.regex.Matcher;

public class MulInstruction extends Instruction {
  public MulInstruction() {
    super(parameter("name"), maybeSpace(), fixed("="), maybeSpace(),
            parameter("a"), maybeSpace(),
            fixed("*"), maybeSpace(),
            parameter("b"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String target = matcher.group("name");
    String a = matcher.group("a");
    String b = matcher.group("b");
    function.mul(function.resolveParameter(target), function.resolveParameter(a), function.resolveParameter(b), context);
  }
}
