package intcode.assembler;

import java.util.regex.Matcher;

public class JumpTrueInstruction extends Instruction {
  public JumpTrueInstruction() {
    super(Token.kw("if"), Token.space(), Token.variable("cmp"), Token.space(), Token.kw("jump"), Token.space(), Token.variable("label"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    String label = matcher.group("label");
    String cmp = matcher.group("cmp");
    function.jump(true, cmp, label);
  }
}
