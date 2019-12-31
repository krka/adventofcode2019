package intcode.assembler;

import java.util.regex.Matcher;

public class OutputInstruction extends Instruction {
  public OutputInstruction() {
    super(Token.fixed("output"), Token.maybeSpace(), Token.fixed("("), Token.maybeSpace(),
            Token.variable("name"), Token.maybeSpace(), Token.fixed(")"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String name = matcher.group("name");
    function.addOutput(name);
  }
}
