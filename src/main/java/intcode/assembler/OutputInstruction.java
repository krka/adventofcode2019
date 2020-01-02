package intcode.assembler;

import java.util.regex.Matcher;

public class OutputInstruction extends Instruction {
  public OutputInstruction() {
    super(fixed("output"), fixed("("),
            parameter("name"), fixed(")"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String name = matcher.group("name");
    function.addOutput(name, context);
  }
}
