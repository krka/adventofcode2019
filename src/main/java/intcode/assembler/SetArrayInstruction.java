package intcode.assembler;

import java.util.regex.Matcher;

public class SetArrayInstruction extends Instruction {
  public SetArrayInstruction() {
    super(Token.parameter("array"), Token.maybeSpace(), Token.fixed("["), Token.maybeSpace(), Token.parameter("index"), Token.maybeSpace(), Token.fixed("]"),
            Token.maybeSpace(), Token.fixed("="), Token.maybeSpace(), Token.parameter("value"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String array = matcher.group("array");
    String index = matcher.group("index");
    String value = matcher.group("value");
    function.setArray(array, index, value);

  }
}
