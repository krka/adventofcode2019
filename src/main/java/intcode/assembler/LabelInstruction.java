package intcode.assembler;

import java.util.regex.Matcher;

public class LabelInstruction extends Instruction {
  public LabelInstruction() {
    super(Token.variable("label"), Token.maybeSpace(), Token.fixed(":"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String label = matcher.group("label");
    function.addLabel(label);
  }
}
