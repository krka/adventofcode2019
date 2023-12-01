package aoc2023;

import util.Day;
import util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day1 implements Day {

  private final List<List<String>> parts;

  static final Map<String, Integer> DIGITS = new HashMap<>();
  static {
    for (int i = 1; i <= 9; i++) {
      DIGITS.put("" + i, i);
    }
  }

  static final Map<String, Integer> DIGITS_SPELLED = new HashMap<>();
  static {
    DIGITS_SPELLED.putAll(DIGITS);
    DIGITS_SPELLED.put("one", 1);
    DIGITS_SPELLED.put("two", 2);
    DIGITS_SPELLED.put("three", 3);
    DIGITS_SPELLED.put("four", 4);
    DIGITS_SPELLED.put("five", 5);
    DIGITS_SPELLED.put("six", 6);
    DIGITS_SPELLED.put("seven", 7);
    DIGITS_SPELLED.put("eight", 8);
    DIGITS_SPELLED.put("nine", 9);
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
            .mapToLong(strings -> strings.stream().mapToLong(x -> digits(x, map)).sum())
            .max().getAsLong();
  }

  private long digits(String x, Map<String, Integer> map) {
    final int[] array = IntStream.range(0, x.length())
            .map(start -> {
              final char c = x.charAt(start);
              if (c >= '0' && c <= '9') {
                return c - '0';
              }
              switch (c) {
                case 'o':
                  return ifMatch(x, start, "one", map);
                case 't': {
                  if (x.length() - start >= 2) {
                    switch (x.charAt(start + 1)) {
                      case 'w': return ifMatch(x, start, "two", map);
                      case 'h': return ifMatch(x, start, "three", map);
                    }
                  }
                  return -1;
                }
                case 'f': {
                  if (x.length() - start >= 2) {
                    switch (x.charAt(start + 1)) {
                      case 'o': return ifMatch(x, start, "four", map);
                      case 'i': return ifMatch(x, start, "five", map);
                    }
                  }
                  return -1;
                }
                case 's': {
                  if (x.length() - start >= 2) {
                    switch (x.charAt(start + 1)) {
                      case 'i': return ifMatch(x, start, "six", map);
                      case 'e': return ifMatch(x, start, "seven", map);
                    }
                  }
                  return -1;
                }
                case 'e': return ifMatch(x, start, "eight", map);
                case 'n': return ifMatch(x, start, "nine", map);
                default: return -1;
              }
            }).filter(v -> v != -1)
            .toArray();

    return array[0] * 10 + array[array.length - 1];
  }

  private int ifMatch(String s, int start, String key, Map<String, Integer> map) {
    final int remaining = s.length() - start;
    final int len = key.length();
    if (remaining < len) {
      return -1;
    }
    if (s.subSequence(start, start + len).equals(key)) {
      return map.getOrDefault(key, -1);
    }
    return -1;
  }

}
