package intcode.assembler;

import java.util.regex.Matcher;

public class AddInstruction extends Instruction {
  public AddInstruction() {
    super(Token.variable("name"), Token.maybeSpace(), Token.kw("="), Token.maybeSpace(),
            Token.variable("a"), Token.maybeSpace(),
            Token.kw("+"), Token.maybeSpace(),
            Token.variable("b"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String target = matcher.group("name");
    String a = matcher.group("a");
    String b = matcher.group("b");
    function.add(target, a, b);
  }
}
