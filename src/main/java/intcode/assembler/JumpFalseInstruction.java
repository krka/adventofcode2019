package intcode.assembler;

import java.util.regex.Matcher;

public class JumpFalseInstruction extends Instruction {
  public JumpFalseInstruction() {
    super(Token.fixed("if"), Token.space(), Token.fixed("not"), Token.space(), Token.variable("cmp"), Token.space(), Token.fixed("jump"), Token.space(), Token.variable("label"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String label = matcher.group("label");
    String cmp = matcher.group("cmp");
    function.jump(false, cmp, label);
  }
}
