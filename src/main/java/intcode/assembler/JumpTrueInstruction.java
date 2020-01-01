package intcode.assembler;

import java.util.regex.Matcher;

public class JumpTrueInstruction extends Instruction {
  public JumpTrueInstruction() {
    super(Token.fixed("if"), Token.space(), Token.parameter("cmp"), Token.space(), Token.fixed("jump"), Token.space(), Token.parameter("label"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String label = matcher.group("label");
    String cmp = matcher.group("cmp");
    function.jump(true, cmp, label);
  }
}
