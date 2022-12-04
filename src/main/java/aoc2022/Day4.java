package aoc2022;

import util.Day;
import util.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day4 implements Day {

  private final List<String> input;

  public Day4(String name) {
    input = Util.readResource(name);
  }

  @Override
  public long solvePart1() {
    return input.stream().filter(s -> !s.isEmpty())
            .mapToLong(s -> solve(s)).sum();
  }

  private long solve(String s) {
    final String[] parts = s.split("[,-]");
    final int a1 = Integer.parseInt(parts[0]);
    final int a2 = Integer.parseInt(parts[1]);
    final int b1 = Integer.parseInt(parts[2]);
    final int b2 = Integer.parseInt(parts[3]);
    final boolean contains = contains(a1, a2, b1, b2) || contains(b1, b2, a1, a2);
    return contains ? 1 : 0;
  }

  private boolean contains(int a, int b, int c, int d) {
    return c >= a && d <= b;
  }

  @Override
  public long solvePart2() {
    return input.stream().filter(s -> !s.isEmpty())
            .mapToLong(s -> solve2(s)).sum();
  }

  private long solve2(String s) {
    final String[] parts = s.split("[,-]");
    final int a1 = Integer.parseInt(parts[0]);
    final int a2 = Integer.parseInt(parts[1]);
    final int b1 = Integer.parseInt(parts[2]);
    final int b2 = Integer.parseInt(parts[3]);
    final boolean overlaps = overlap(a1, a2, b1, b2) || overlap(b1, b2, a1, a2);
    return overlaps ? 1 : 0;
  }

  private boolean overlap(int a1, int a2, int b1, int b2) {
    return a1 <= b1 && b1 <= a2 || a1 <= b2 && b2 <= a2;
  }
}
