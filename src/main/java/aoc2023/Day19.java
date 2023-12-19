package aoc2023;

import util.Day;
import util.Interval;
import util.Pair;
import util.Splitter;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.Util.patch;

public class Day19 implements Day {
  private static final Splitter SPLITTER_RULE = Splitter.of("[{}]");
  private static final Splitter SPLITTER_COMMA = Splitter.of(",").withoutDelim();
  private static final Splitter SPLITTER_MAP_PARTS = Splitter.of("[\\{,=\\}]").withoutDelim();
  public static final Splitter SPLITTER_RULE_PART = Splitter.of("[<>:]");

  private final List<List<String>> lines;
  private final Map<String, List<Rule>> rules;
  private final ArrayList<Map<String, Integer>> maps;

  public Day19(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    rules = new HashMap<>();
    for (String line : lines.get(0)) {
      final List<String> parts = SPLITTER_RULE.split(line);
      final String ruleName = parts.get(0);
      final List<String> split = SPLITTER_COMMA.split(parts.get(2));
      final List<Rule> ruleList = split.stream().map(Day19::toRulePart).collect(Collectors.toList());
      rules.put(ruleName, ruleList);
    }

    maps = new ArrayList<>();
    for (String values : lines.get(1)) {
      final List<String> split = SPLITTER_MAP_PARTS.split(values);
      Map<String, Integer> values2 = new HashMap<>();
      for (int i = 0; i < split.size(); i += 2) {
        values2.put(split.get(i), Integer.parseInt(split.get(i + 1)));
      }
      maps.add(values2);
    }
  }

  private static Rule toRulePart(String s) {
    final List<String> split = SPLITTER_RULE_PART.split(s);
    if (split.size() == 1) {
      return new Rule("", "", -1, s);
    }
    if (split.size() == 5) {
      final String var = split.get(0);
      final String op = split.get(1);
      final int operand = Integer.parseInt(split.get(2));
      String target = split.get(4);
      return new Rule(var, op, operand, target);
    }
    throw new RuntimeException();
  }

  @Override
  public long solvePart1() {
    return maps.stream().mapToLong(this::solveMap).sum();
  }

  private long solveMap(Map<String, Integer> map) {
    String ruleName = "in";
    while (true) {
      switch (ruleName) {
        case "A": return map.values().stream().mapToLong(Integer::longValue).sum();
        case "R": return 0;
      }
      ruleName = nextTarget(map, ruleName);
    }
  }

  private String nextTarget(Map<String, Integer> map, String rulename) {
    for (Rule rule : rules.get(rulename)) {
      if (rule.isDirectTarget()) {
        return rule.target;
      }
      final int actualValue = map.get(rule.varName);
      if (rule.matches(actualValue)) {
        return rule.target;
      }
    }
    throw new RuntimeException();
  }

  @Override
  public long solvePart2() {
    Interval x = Interval.of(1, 4001);
    return count("in", Map.of("x", x, "m", x, "a", x, "s", x));
  }

  private long count(String ruleName, Map<String, Interval> intervals) {
    switch (ruleName) {
      case "A": return multiply(intervals);
      case "R": return 0;
    }
    long sum = 0;
    for (Rule rule : rules.get(ruleName)) {
      if (rule.isDirectTarget()) {
        return sum + count(rule.target, intervals);
      }
      var split = rule.split(intervals.get(rule.varName));
      if (split.a().nonEmpty()) {
        sum += count(rule.target, patch(intervals, rule.varName, split.a()));
      }
      if (split.b().isEmpty()) {
        return sum;
      }
      intervals = patch(intervals, rule.varName, split.b());
    }
    throw new RuntimeException();
  }

  private long multiply(Map<String, Interval> intervals) {
    long prod = 1;
    for (Interval value : intervals.values()) {
      prod *= value.length();
    }
    return prod;
  }

  private static class Rule {
    final String varName;
    final boolean negate;
    final int splitPoint;
    final String target;

    public Rule(String var, String op, int operand, String target) {
      this.varName = var;
      if (op.equals("<")) {
        negate = false;
        splitPoint = operand;
      } else if (op.equals(">")){
        negate = true;
        splitPoint = operand + 1;
      } else {
        negate = false;
        splitPoint = -1;
      }
      this.target = target;
    }

    private boolean isDirectTarget() {
      return varName.isEmpty();
    }

    public Pair<Interval, Interval> split(Interval interval) {
      if (splitPoint < 0) {
        throw new RuntimeException();
      }
      final Pair<Interval, Interval> pair = interval.split(splitPoint);
      if (negate) {
        return pair.swap();
      } else {
        return pair;
      }
    }

    public boolean matches(int value) {
      if (splitPoint < 0) {
        throw new RuntimeException();
      }
      return negate != value < splitPoint;
    }
  }
}

