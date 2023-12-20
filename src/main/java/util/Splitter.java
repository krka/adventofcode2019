package util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Splitter {
  private final Pattern pattern;
  private final boolean includeDelim;

  private Splitter(Pattern pattern, boolean includeDelim) {
    this.pattern = pattern;
    this.includeDelim = includeDelim;
  }

  public static Splitter withDelim(Pattern pattern) {
    return new Splitter(pattern, true);
  }

  public static Splitter withDelim(String pattern) {
    return withDelim(Pattern.compile(pattern));
  }

  public static Splitter withoutDelim(Pattern pattern) {
    return new Splitter(pattern, false);
  }

  public static Splitter withoutDelim(String pattern) {
    return withoutDelim(Pattern.compile(pattern));
  }

  public List<String> split(String s) {
    final Matcher matcher = pattern.matcher(s);
    final ArrayList<String> res = new ArrayList<>();
    int pos = 0;
    while (matcher.find()) {
      final int start = matcher.start();
      final int end = matcher.end();
      maybeAdd(res, s, pos, start);
      if (includeDelim) {
        maybeAdd(res, s, start, end);
      }
      pos = end;
    }
    maybeAdd(res, s, pos, s.length());
    return res;
  }

  private static void maybeAdd(List<String> res, String s, int a, int b) {
    if (a < b) {
      res.add(s.substring(a, b));
    }
  }

  @Override
  public String toString() {
    return "Splitter{" +
            "pattern=" + pattern +
            ", includeDelim=" + includeDelim +
            '}';
  }
}
