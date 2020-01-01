package intcode.assembler;

import java.util.regex.Matcher;

public class DeclareArray extends Instruction {
  protected DeclareArray() {
    super(Instruction.pattern(
            Token.fixed("array"), Token.maybeSpace(),
            Token.fixed("["), Token.maybeSpace(), Token.integer("size"), Token.maybeSpace(), Token.fixed("]"), Token.maybeSpace(),
            Token.parameter("name")));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    assembler.declareArray(matcher.group("name"), Integer.parseInt(matcher.group("size")));
  }
}
