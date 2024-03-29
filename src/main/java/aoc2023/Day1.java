package aoc2023;

import util.Day;
import util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day1 implements Day {

  private final List<List<String>> parts;

  static final Map<String, Integer> DIGITS = new HashMap<>();
  static {
    for (int i = 1; i <= 9; i++) {
      DIGITS.put("" + i, i);
    }
  }

  private static final String SPELLED = "one two three four five six seven eight nine";

  static final Map<String, Integer> DIGITS_SPELLED = new HashMap<>();
  static {
    DIGITS_SPELLED.putAll(DIGITS);
    int i = 0;
    for (String s : SPELLED.split(" ")) {
      DIGITS_SPELLED.put(s, ++i);
    }
  }

  public Day1(String name) {
    parts = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
  }

  @Override
  public long solvePart1() {
    return sum(DIGITS);
  }

  @Override
  public long solvePart2() {
    return sum(DIGITS_SPELLED);
  }

  private long sum(Map<String, Integer> map) {
    return parts.stream()
            .mapToLong(strings -> strings.stream().mapToLong(line -> digits(line, map)).sum())
            .max().getAsLong();
  }

  private long digits(String line, Map<String, Integer> map) {
    int first = find(line, map, 0, 1);
    int last = find(line, map, line.length() - 1, -1);
    return first * 10 + last;
  }

  private int find(String line, Map<String, Integer> map, int start, int step) {
    while (start >= 0 && start < line.length()) {
      for (Map.Entry<String, Integer> entry : map.entrySet()) {
        final String key = entry.getKey();
        if (start + key.length() <= line.length()) {
          if (line.subSequence(start, start + key.length()).equals(key)) {
            return entry.getValue();
          }
        }
      }
      start += step;
    }
    throw new RuntimeException();
  }

}
