package intcode.assembler;

import java.util.regex.Matcher;

public class LessThanInstruction extends Instruction {
  public LessThanInstruction() {
    super(Token.parameter("name"), Token.maybeSpace(), Token.fixed("="), Token.maybeSpace(),
            Token.parameter("a"), Token.maybeSpace(),
            Token.fixed("<"), Token.maybeSpace(),
            Token.parameter("b"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String target = matcher.group("name");
    String a = matcher.group("a");
    String b = matcher.group("b");
    function.lessThan(target, a, b);
  }
}
