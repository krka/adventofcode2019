package intcode.assembler;

import java.util.regex.Matcher;

public class InputInstruction extends Instruction {
  public InputInstruction() {
    super(Token.variable("name"), Token.maybeSpace(), Token.fixed("="), Token.maybeSpace(), Token.fixed("input"), Token.maybeSpace(), Token.fixed("("), Token.maybeSpace(), Token.fixed(")"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String name = matcher.group("name");
    function.addInput(name);
  }
}
