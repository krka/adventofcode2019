package aoc2020;

import org.junit.Test;
import util.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Day19Test {

  public static final String DAY = "19";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(122, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(2, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(287, solvePart2(MAIN_INPUT));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    List<List<String>> parts = input.stream().collect(Util.splitBy(String::isEmpty));

    Map<Integer, Rule> ruleDefs = parseRules(parts);

    Rule rule0 = ruleDefs.get(0);
    System.out.println("Rule: '" + rule0 + "'");

    List<String> messages = parts.get(1);
    return messages.stream().filter(rule0::matches).count();
  }

  private Map<Integer, Rule> parseRules(List<List<String>> parts) {
    Map<Integer, Rule> ruleDefs = new HashMap<>();
    for (String ruleDef : parts.get(0)) {
      String[] split = ruleDef.split(":");
      ruleDefs.put(Integer.parseInt(split[0]), parseRule(ruleDefs, split[1].trim()));
    }
    return ruleDefs;
  }

  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);
    List<List<String>> parts = input.stream().collect(Util.splitBy(String::isEmpty));

    Map<Integer, Rule> ruleDefs = parseRules(parts);
    ruleDefs.put(8, parseRule(ruleDefs, "42 | 42 8"));
    ruleDefs.put(11, parseRule(ruleDefs, "42 31 | 42 11 31"));

    Rule rule0 = ruleDefs.get(0);

    List<String> messages = parts.get(1);
    return messages.stream().filter(rule0::matches).count();
  }

  private Rule parseRule(Map<Integer, Rule> ruleDefs, String ruleDef) {
    ruleDef = ruleDef.trim();
    if (ruleDef.contains("|")) {
      return new AltRule(Arrays.stream(ruleDef.split("\\|"))
              .map(alt -> parseRule(ruleDefs, alt))
              .collect(Collectors.toList()));
    }

    if (ruleDef.contains(" ")) {
      return new SequenceRule(Arrays.stream(ruleDef.split("\\s+"))
              .map(s -> parseRule(ruleDefs, s))
              .collect(Collectors.toList()));
    }

    if (ruleDef.charAt(0) == '"') {
      return new ConstRule(ruleDef.substring(1, ruleDef.length() - 1));
    }

    return new RuleRef(ruleDefs, Integer.parseInt(ruleDef));
  }

  interface Rule {
    Set<Integer> matches(String s, int index);

    default boolean matches(String s) {
      return matches(s, 0).contains(s.length());
    }

    default void debug(String s, int index, Set<Integer> ans) {
      //System.out.printf("pattern=%s: %s[%d:]=%s: %s%n", toString(), s, index, s.substring(index), ans);
    }

  }

  private static class ConstRule implements Rule {
    private final String pattern;
    private final int length;

    ConstRule(String s) {
      pattern = s;
      length = s.length();
    }

    @Override
    public Set<Integer> matches(String s, int index) {
      int end = index + length;
      if (end > s.length()) {
        return Set.of();
      }
      if (s.substring(index, end).equals(pattern)) {
        return Set.of(end);
      }
      return Set.of();
    }

    @Override
    public String toString() {
      return pattern;
    }
  }

  private static class RuleRef implements Rule {
    private final Map<Integer, Rule> ruleDefs;
    private final int ruleId;
    private Rule rule;

    RuleRef(Map<Integer, Rule> ruleDefs, int ruleId) {
      this.ruleDefs = ruleDefs;
      this.ruleId = ruleId;
    }

    @Override
    public Set<Integer> matches(String s, int index) {
      return getRule().matches(s, index);
    }

    private Rule getRule() {
      if (rule == null) {
        rule = ruleDefs.get(ruleId);
      }
      return rule;
    }

    @Override
    public String toString() {
      return getRule().toString();
    }
  }

  private static class AltRule implements Rule {

    private final List<Rule> rules;

    AltRule(List<Rule> rules) {
      this.rules = rules;
    }

    @Override
    public Set<Integer> matches(String s, int index) {
      Set<Integer> cur = rules.stream()
              .map(rule -> rule.matches(s, index))
              .flatMap(Collection::stream)
              .collect(Collectors.toSet());
      debug(s, index, cur);
      return cur;
    }

    @Override
    public String toString() {
      return "(" + rules.stream().map(Object::toString).collect(Collectors.joining("|")) + ")";
    }
  }

  private static class SequenceRule implements Rule {
    private final List<Rule> rules;

    SequenceRule(List<Rule> rules) {
      this.rules = rules;
    }

    @Override
    public Set<Integer> matches(String s, int index) {
      Set<Integer> cur = Set.of(index);
      for (Rule rule : rules) {
        cur = cur.stream()
                .map(index2 -> rule.matches(s, index2))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
      }
      debug(s, index, cur);
      return cur;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (Rule rule : rules) {
        sb.append(rule.toString());
      }
      return sb.toString();
    }
  }
}
