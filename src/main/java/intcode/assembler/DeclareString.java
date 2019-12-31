package intcode.assembler;

import java.util.regex.Matcher;

public class DeclareString extends Instruction {
  protected DeclareString() {
    super(Instruction.pattern(Token.kw("string"), Token.space(), Token.variable("name"), Token.maybeSpace(), Token.kw("="), Token.maybeSpace(), Token.stringConstant("value")));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    assembler.declareString(matcher.group("name"), matcher.group("value"));
  }
}
