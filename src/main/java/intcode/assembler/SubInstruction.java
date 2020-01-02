package intcode.assembler;

import java.util.regex.Matcher;

public class SubInstruction extends Instruction {
  public SubInstruction() {
    super(parameter("name"), maybeSpace(), fixed("="), maybeSpace(),
            parameter("a"), maybeSpace(),
            fixed("-"), maybeSpace(),
            parameter("b"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    Parameter a = function.resolveParameter(matcher.group("a"));
    Parameter b = function.resolveParameter(matcher.group("b"));
    Parameter target = function.resolveParameter(matcher.group("name"));

    assembler.ensureTempSpaceSize(1);
    Variable tmp = assembler.tempSpace.get(0);
    function.mul(tmp, Constant.MINUS_ONE, b, context);
    function.add(context, target, a, tmp);
  }
}
