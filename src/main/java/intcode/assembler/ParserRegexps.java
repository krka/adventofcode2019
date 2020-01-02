package intcode.assembler;

import java.util.regex.Pattern;

public class ParserRegexps {
  static final String MAYBE_SPACE = "\\s*";

  static String fixed(String s) {
    return MAYBE_SPACE + Pattern.quote(s) + MAYBE_SPACE;
  }

  static String integer(String name) {
    return MAYBE_SPACE + "(?<" + name + ">\\-?[0-9]+)" + MAYBE_SPACE;
  }

  static String parameter(String name) {
    return MAYBE_SPACE + "(?<" + name + ">[^\\s,()<>=!\\[\\]]+)" + MAYBE_SPACE;
  }

  // TODO: remove unnecessary usages of this
  static String maybeSpace() {
    return MAYBE_SPACE;
  }

  static String space() {
    return "\\s+";
  }

  static String stringConstant(String name) {
    return MAYBE_SPACE + "\"(?<" + name + ">[^\"]+)\"" + MAYBE_SPACE;
  }

  static String anything() {
    return ".*";
  }

  static String commaList(String name) {
    return MAYBE_SPACE + "(?<" + name + ">[^()]*)" + MAYBE_SPACE;
  }

}
