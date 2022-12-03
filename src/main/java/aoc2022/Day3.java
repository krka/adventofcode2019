package aoc2022;

import util.Day;
import util.Util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day3 implements Day {

  private final List<String> input;

  public Day3(String name) {
    input = Util.readResource(name);
  }

  @Override
  public long solvePart1() {
    return input.stream().mapToLong(this::split).sum();
  }

  private long split(String s) {
    long score = 0;
    int halfWay = s.length() / 2;
    final Set<Character> left = new HashSet<>();
    for (int i = 0; i < halfWay; i++) {
      left.add(s.charAt(i));
    }
    final Set<Character> right = new HashSet<>();
    for (int i = halfWay; i < s.length(); i++) {
      final char c = s.charAt(i);
      if (left.contains(c)) {
        if (right.add(c)) {
          score += prio(c);
        }
      }
    }
    return score;
  }

  private long prio(int c) {
    if (c >= 'a' && c <= 'z') {
      return 1 + c - 'a';
    }
    if (c >= 'A' && c <= 'Z') {
      return 27 + c - 'A';
    }
    throw new RuntimeException();
  }

  @Override
  public long solvePart2() {
    return Util.partition(input, 3).stream().mapToLong(this::common2).sum();
  }

  private long common2(List<String> strings) {
    if (strings.size() < 3) {
      return 0;
    }
    return common(strings.get(0), strings.get(1), strings.get(2));
  }

  private long common(String line1, String line2, String line3) {
    final Set<Long> set = line1.chars().mapToObj(this::prio).collect(Collectors.toSet());
    set.retainAll(line2.chars().mapToObj(this::prio).collect(Collectors.toList()));
    set.retainAll(line3.chars().mapToObj(this::prio).collect(Collectors.toList()));
    if (set.size() != 1) {
      throw new RuntimeException();
    }
    return set.iterator().next();
  }
}
