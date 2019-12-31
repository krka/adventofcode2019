package intcode.assembler;

import java.util.regex.Matcher;

public class IncludeResource extends Instruction {
  protected IncludeResource() {
    super(Instruction.pattern(Token.kw("#include"), Token.space(), Token.variable("name")));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function) {
    assembler.includeResource(matcher.group("name"));
  }
}
