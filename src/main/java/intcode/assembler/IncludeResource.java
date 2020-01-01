package intcode.assembler;

import java.util.regex.Matcher;

public class IncludeResource extends Instruction {
  protected IncludeResource() {
    super(Instruction.pattern(Token.fixed("#include"), Token.space(), Token.parameter("name")));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    assembler.includeResource(matcher.group("name"));
  }
}
