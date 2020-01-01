package intcode.assembler;

import java.util.regex.Matcher;

public class JumpAlwaysInstruction extends Instruction {
  public JumpAlwaysInstruction() {
    super(Token.fixed("jump"), Token.space(), Token.parameter("label"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String label = matcher.group("label");
    function.jump(false, "0", label);
  }
}
