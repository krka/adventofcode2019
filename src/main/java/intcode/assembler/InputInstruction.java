package intcode.assembler;

import java.util.regex.Matcher;

public class InputInstruction extends Instruction {
  public InputInstruction() {
    super(parameter("name"), maybeSpace(), fixed("="), maybeSpace(), fixed("input"), maybeSpace(), fixed("("), maybeSpace(), fixed(")"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String name = matcher.group("name");
    function.addInput(name, context);
  }
}
