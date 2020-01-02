package intcode.assembler;

import java.util.regex.Matcher;

public class GetArrayInstruction extends Instruction {
  public GetArrayInstruction() {
    super(parameter("target"), maybeSpace(), fixed("="), maybeSpace(),
            parameter("array"), maybeSpace(), fixed("["), maybeSpace(), parameter("index"), maybeSpace(), fixed("]"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String array = matcher.group("array");
    String index = matcher.group("index");
    String target = matcher.group("target");
    function.getArray(array, index, target, context);

  }
}
