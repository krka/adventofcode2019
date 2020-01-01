package intcode.assembler;

import java.util.regex.Matcher;

public class DeclareString extends Instruction {
  protected DeclareString() {
    super(Instruction.pattern(Token.fixed("string"), Token.space(), Token.parameter("name"), Token.maybeSpace(), Token.fixed("="), Token.maybeSpace(), Token.stringConstant("value")));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String name = matcher.group("name");
    String value = matcher.group("value");
    assembler.declareString(name, value, context);
  }
}
