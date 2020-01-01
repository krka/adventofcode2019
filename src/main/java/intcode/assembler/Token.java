package intcode.assembler;

import java.util.regex.Pattern;

public class Token {
  final String regexp;

  public Token(String regexp) {
    this.regexp = regexp;
  }

  static Token fixed(String s) {
    return new Token(Pattern.quote(s));
  }

  public static Token parameter(String groupName) {
    return new Token("(?<" + groupName + ">[^\\s,()<>=!\\[\\]]+)");
  }

  public static Token space() {
    return new Token("\\s+");
  }

  public static Token maybeSpace() {
    return new Token("\\s*");
  }

  public static Token integer(String groupName) {
    return new Token("(?<" + groupName + ">\\-?[0-9]+)");
  }

  public static Token stringConstant(String groupName) {
    return new Token("\"(?<" + groupName + ">[^\"]+)\"");
  }

  public static Token anything() {
    return new Token(".*");
  }

  public static Token commaList(String groupName) {
    return new Token("(?<" + groupName + ">[^()]*)");
  }
}
