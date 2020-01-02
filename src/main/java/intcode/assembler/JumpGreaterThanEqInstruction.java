package intcode.assembler;

import java.util.regex.Matcher;

public class JumpGreaterThanEqInstruction extends Instruction {
  public JumpGreaterThanEqInstruction() {
    super(fixed("if"), space(), parameter("a"), fixed(">="), parameter("b"), fixed("jump"), space(), parameter("label"));
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