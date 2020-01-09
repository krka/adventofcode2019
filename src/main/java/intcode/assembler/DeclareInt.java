package intcode.assembler;

import java.util.regex.Matcher;

public class DeclareInt extends Instruction {
  protected DeclareInt() {
    super(fixed("int"), space(), parameter("name"), fixed("="), integer("value"));
  }

  @Override
  protected void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context) {
    function.declareInt(matcher.group("name"), matcher.group("value"), context);
  }
}
