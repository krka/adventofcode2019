package intcode.assembler;

import java.util.regex.Matcher;

public class JumpLessThanInstruction extends Instruction {
  public JumpLessThanInstruction() {
    super(fixed("if"), space(), parameter("a"), maybeSpace(), fixed("<"), maybeSpace(),
            parameter("b"), maybeSpace(), fixed("jump"), space(), parameter("label"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String label = matcher.group("label");
    String a = matcher.group("a");
    String b = matcher.group("b");

    try (TempVariable tmpVariable = assembler.tempSpace.getAny()) {
      function.lessThan(tmpVariable, a, b, context);
      function.jump(true, tmpVariable, label, "# jump if true");
    }
  }
}
