package intcode.assembler;

import java.util.regex.Matcher;

public class SetInstruction extends Instruction {
  public SetInstruction() {
    super(Token.variable("name"), Token.maybeSpace(), Token.fixed("="), Token.maybeSpace(),
            Token.variable("a"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String target = matcher.group("name");
    String a = matcher.group("a");
    function.add(target, a, "0");
  }
}
