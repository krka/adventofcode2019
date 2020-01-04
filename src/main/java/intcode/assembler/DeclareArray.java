package intcode.assembler;

import java.util.regex.Matcher;

public class DeclareArray extends Instruction {
  protected DeclareArray() {
    super(fixed("array"), fixed("["), parameter("size"), fixed("]"), parameter("name"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    String name = matcher.group("name");
    String size = matcher.group("size");
    function.declareArray(name, size, context);
  }
}
