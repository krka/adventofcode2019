package intcode.assembler;

import java.util.regex.Matcher;

public class GetArrayInstruction extends Instruction {
  public GetArrayInstruction() {
    super(Token.parameter("target"), Token.maybeSpace(), Token.fixed("="), Token.maybeSpace(),
            Token.parameter("array"), Token.maybeSpace(), Token.fixed("["), Token.maybeSpace(), Token.parameter("index"), Token.maybeSpace(), Token.fixed("]"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String array = matcher.group("array");
    String index = matcher.group("index");
    String target = matcher.group("target");
    function.getArray(array, index, target);

  }
}
