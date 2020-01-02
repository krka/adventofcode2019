package intcode.assembler;

import java.util.regex.Matcher;

public class JumpAlwaysInstruction extends Instruction {
  public JumpAlwaysInstruction() {
    super(fixed("jump"), space(), parameter("label"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String label = matcher.group("label");
    function.jump(false, "0", label, context);
  }
}
