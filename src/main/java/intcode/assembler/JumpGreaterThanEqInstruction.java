package intcode.assembler;

import java.util.regex.Matcher;

public class JumpGreaterThanEqInstruction extends Instruction {
  public JumpGreaterThanEqInstruction() {
    super(Token.fixed("if"), Token.space(), Token.parameter("a"), Token.maybeSpace(), Token.fixed(">="), Token.maybeSpace(),
            Token.parameter("b"), Token.maybeSpace(), Token.fixed("jump"), Token.space(), Token.parameter("label"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String label = matcher.group("label");
    String a = matcher.group("a");
    String b = matcher.group("b");

    assembler.ensureTempSpaceSize(1);
    Variable tmpVariable = assembler.tempSpace.get(0);
    function.lessThan(tmpVariable, a, b, context);
    function.jump(false, tmpVariable, label, "# jump if false");
  }
}
