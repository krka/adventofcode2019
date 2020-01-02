package intcode.assembler;

import java.util.regex.Matcher;

public class JumpTrueInstruction extends Instruction {
  public JumpTrueInstruction() {
    super(fixed("if"), space(), parameter("cmp"), space(), fixed("jump"), space(), parameter("label"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String label = matcher.group("label");
    String cmp = matcher.group("cmp");
    function.jump(true, cmp, label, context);
  }
}