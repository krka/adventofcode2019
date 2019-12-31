package intcode.assembler;

import java.util.regex.Matcher;

public class DeclareInt extends Instruction {
  protected DeclareInt() {
    super(Instruction.pattern(Token.fixed("int"), Token.space(), Token.variable("name"), Token.maybeSpace(), Token.fixed("="), Token.maybeSpace(), Token.integer("value")));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    assembler.declareInt(matcher.group("name"), matcher.group("value"));
  }
}
