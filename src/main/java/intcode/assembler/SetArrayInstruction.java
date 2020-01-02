package intcode.assembler;

import java.util.regex.Matcher;

public class SetArrayInstruction extends Instruction {
  public SetArrayInstruction() {
    super(parameter("array"), fixed("["), parameter("index"), fixed("]"),
            fixed("="), parameter("value"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String array = matcher.group("array");
    String index = matcher.group("index");
    String value = matcher.group("value");
    function.setArray(array, index, value, context);

  }
}
