package aoc2020;

import util.Util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {

  private final List<String> input;

  public Day2(String name) {
    input = Util.readResource(name);
  }

  public int solvePart1() {
    Pattern pattern = Pattern.compile("(?<min>\\d+)\\-(?<max>\\d+) (?<c>[a-z]): (?<pw>.*)");
    int count2 = 0;
    for (String s : input) {
      Matcher matcher = pattern.matcher(s);
      if (matcher.matches()) {
        int min = Integer.parseInt(matcher.group("min"));
        int max = Integer.parseInt(matcher.group("max"));
        char c = matcher.group("c").charAt(0);
        String pw = matcher.group("pw");
        long count = pw.chars().filter(x -> x == c).count();
        if (count >= min && count <= max) {
          count2++;
        }
      } else {
        throw new RuntimeException("Failed on " + s);
      }
    }
    return count2;
  }

  public int solvePart2() {
    Pattern pattern = Pattern.compile("(?<p1>\\d+)\\-(?<p2>\\d+) (?<c>[a-z]): (?<pw>.*)");
    int count2 = 0;
    for (String s : input) {
      Matcher matcher = pattern.matcher(s);
      if (matcher.matches()) {
        int p1 = Integer.parseInt(matcher.group("p1"));
        int p2 = Integer.parseInt(matcher.group("p2"));
        char c = matcher.group("c").charAt(0);
        String pw = matcher.group("pw");
        if ((pw.charAt(p1 - 1) == c) != (pw.charAt(p2 - 1) == c)) {
          count2++;
        }
      } else {
        throw new RuntimeException("Failed on " + s);
      }
    }
    return count2;
  }
}
