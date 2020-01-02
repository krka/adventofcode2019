package intcode.assembler;

import java.util.regex.Matcher;

public class JumpEqInstruction extends Instruction {
  public JumpEqInstruction() {
    super(fixed("if"), space(), parameter("a"), maybeSpace(), fixed("=="), maybeSpace(),
            parameter("b"), maybeSpace(), fixed("jump"), space(), parameter("label"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String label = matcher.group("label");
    String a = matcher.group("a");
    String b = matcher.group("b");

    assembler.getTemp(1);
    Variable tmpVariable = assembler.getTemp(0);
    function.eq(tmpVariable, a, b, context);
    function.jump(true, tmpVariable, label, "# jump if true");
  }
}
