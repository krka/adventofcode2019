package intcode.assembler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Instruction extends ParserRegexps {
  final Pattern pattern;

  protected Instruction(String... tokens) {
    this.pattern = pattern(tokens);
  }

  static Pattern pattern(String... tokens) {
    StringBuilder sb = new StringBuilder();
    for (String token : tokens) {
      sb.append(token);
    }
    return Pattern.compile(sb.toString());
  }

  public boolean apply(String line, Assembler assembler, Assembler.Function function, String context) {
    Matcher matcher = applyMatch(line);
    if (matcher.matches()) {
      apply(matcher, assembler, function, context);
      return true;
    }
    return false;
  }

  Matcher applyMatch(String line) {
    return pattern.matcher(line);
  }

  protected abstract void apply(Matcher matcher, Assembler assembler, Assembler.Function function, String context);
}
