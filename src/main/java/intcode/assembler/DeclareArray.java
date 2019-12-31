package intcode.assembler;

import java.util.regex.Matcher;

public class DeclareArray extends Instruction {
  protected DeclareArray() {
    super(Instruction.pattern(
            Token.kw("array"), Token.maybeSpace(),
            Token.kw("["), Token.maybeSpace(), Token.integer("size"), Token.maybeSpace(), Token.kw("]"), Token.maybeSpace(),
            Token.variable("name")));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    assembler.declareArray(matcher.group("name"), Integer.parseInt(matcher.group("size")));
  }
}
