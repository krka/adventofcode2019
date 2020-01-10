package intcode.assembler;

import java.util.regex.Matcher;

public class IncludeResource extends Instruction {
  protected IncludeResource() {
    super(fixed("#include"), space(), parameter("name"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    assembler.includeResource(matcher.group("name"));
  }
}
