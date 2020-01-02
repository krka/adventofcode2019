package intcode.assembler;

import java.util.regex.Matcher;

public class DeclareString extends Instruction {
  protected DeclareString() {
    super(fixed("string"), space(), parameter("name"), fixed("="), stringConstant("value"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String name = matcher.group("name");
    String value = matcher.group("value");
    assembler.declareString(name, value, context);
  }
}
