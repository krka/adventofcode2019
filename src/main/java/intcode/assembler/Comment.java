package intcode.assembler;

import java.util.regex.Matcher;

public class Comment extends Instruction {
  protected Comment() {
    super(fixed("#"), anything());
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.IntCodeFunction function, String context) {

  }
}
