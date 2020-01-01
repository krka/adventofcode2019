package intcode.assembler;

import java.util.regex.Matcher;

public class Comment extends Instruction {
  protected Comment() {
    super(Instruction.pattern(Token.fixed("#"), Token.anything()));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {

  }
}
