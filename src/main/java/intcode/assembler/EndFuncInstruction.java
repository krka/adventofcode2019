package intcode.assembler;

import java.util.regex.Matcher;

public class EndFuncInstruction extends Instruction {
  public EndFuncInstruction() {
    super(Token.fixed("endfunc"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    if (function == assembler.main) {
      throw new RuntimeException("Can not return from main");
    }
    assembler.setFunction(assembler.main);
  }
}
