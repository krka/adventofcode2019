package intcode.assembler;

import java.util.regex.Matcher;

public class HaltInstruction extends Instruction {
  public HaltInstruction() {
    super(Token.fixed("halt"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    function.addHalt();
  }
}
