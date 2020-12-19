package aoc2020;

import org.junit.Test;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    Map<Integer, String> ruleDefs = mapDefs(parts.get(0));

    Map<Integer, Rule> rules = new HashMap<>();
    Rule rule0 = readRule(rules, ruleDefs, 0);

    List<String> messages = parts.get(1);
    return messages.stream().filter(rule0::matches).count();
  }

  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);
    List<List<String>> parts = input.stream().collect(Util.splitBy(String::isEmpty));

    Map<Integer, String> ruleDefs = mapDefs(parts.get(0));
    ruleDefs.put(8, "42 | 42 8");
    ruleDefs.put(11, "42 31 | 42 11 31");

    Map<Integer, Rule> ruleMap = new HashMap<>();
    Rule rule0 = readRule(ruleMap, ruleDefs, 0);

    List<String> messages = parts.get(1);
    return messages.stream().filter(rule0::matches).count();
  }

  private Map<Integer, String> mapDefs(List<String> ruleList) {
    Map<Integer, String> ruleDefs = new HashMap<>();
    for (String rule : ruleList) {
      String[] split = rule.split(":");
      ruleDefs.put(Integer.parseInt(split[0].trim()), split[1].trim());
    }
    return ruleDefs;
  }

  private Rule readRule(Map<Integer, Rule> ruleMap, Map<Integer, String> ruleInput, int i) {
    if (ruleMap.containsKey(i)) {
      return ruleMap.get(i);
    }
    String s = ruleInput.get(i);
    String[] alternatives = s.split("\\|");
    Rule rule = new Rule();
    rule.id = i;
    ruleMap.put(i, rule);
    for (String alt : alternatives) {
      alt = alt.trim();
      List<Rule> alt2 = new ArrayList<>();
      for (String part : alt.split(" ")) {
        part = part.trim();
        if (part.startsWith("\"")) {
          if (part.length() != 3 || rule.c != 0) {
            throw new RuntimeException();
          }
          rule.c = part.charAt(1);
        } else {
          alt2.add(readRule(ruleMap, ruleInput, Integer.parseInt(part)));
        }
      }
      if (!alt2.isEmpty()) {
        rule.alts.add(alt2);
      }
    }
    if (rule.c != 0 && !rule.alts.isEmpty()) {
      throw new RuntimeException();
    }
    return rule;
  }

  private class Rule {
    int id;
    List<List<Rule>> alts = new ArrayList<>();
    char c;

    public boolean matches(String message) {
      return matches(message, 0).contains(message.length());
    }

    private Set<Integer> matches(String message, int i) {
      if (i >= message.length()) {
        return Set.of();
      }
      if (c != 0) {
        if (message.charAt(i) == c) {
          return Set.of(i + 1);
        } else {
          return Set.of();
        }
      }

      Set<Integer> all = new HashSet<>();
      for (List<Rule> alt : alts) {
        Set<Integer> cur = Set.of(i);
        for (Rule rule : alt) {
          Set<Integer> next = new HashSet<>();
          for (Integer i2 : cur) {
            next.addAll(rule.matches(message, i2));
          }
          cur = next;
        }
        all.addAll(cur);
      }
      return all;
    }

    @Override
    public String toString() {
      return "Rule " + id;
    }
  }
}
