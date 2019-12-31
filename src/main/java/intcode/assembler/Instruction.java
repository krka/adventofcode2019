package intcode.assembler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Instruction {
  private final Pattern pattern;

  protected Instruction(Pattern pattern) {
    this.pattern = pattern;
  }

  protected Instruction(Token... tokens) {
    this.pattern = pattern(tokens);
  }

  static Pattern pattern(Token... tokens) {
    StringBuilder sb = new StringBuilder();
    for (Token token : tokens) {
      sb.append(token.regexp);
    }
    return Pattern.compile(sb.toString());
  }

  public boolean apply(String line, Assembler assembler, Assembler.Function function) {
    Matcher matcher = pattern.matcher(line);
    if (matcher.matches()) {
      apply(matcher, assembler, function);
      return true;
    }
    return false;
  }

  protected abstract void apply(Matcher matcher, Assembler assembler, Assembler.Function function);
}
