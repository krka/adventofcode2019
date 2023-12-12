package aoc2023;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day12 implements Day {

  private final List<List<String>> lines;

  public Day12(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
  }

  @Override
  public long solvePart1() {
    return lines.get(0).stream().mapToLong(this::solve).sum();
  }

  @Override
  public long solvePart2() {
    return lines.get(0).stream().mapToLong(this::solve2).sum();
  }

  private long solve2(String s) {
    final String[] split = s.split(" ");
    final String data = split[0];
    final String[] rawGroups = split[1].split(",");

    final List<Integer> groups = Arrays.stream(rawGroups).map(Integer::parseInt).collect(Collectors.toList());
    final StringBuilder data2 = new StringBuilder();
    final List<Integer> groups2 = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      if (i > 0) {
        data2.append("?");
      }
      data2.append(data);
      groups2.addAll(groups);
    }

    return solve(data2.toString(), groups2);
  }

  private long solve(String s) {
    final String[] split = s.split(" ");
    final String data = split[0];
    final String[] rawGroups = split[1].split(",");

    final List<Integer> groups = Arrays.stream(rawGroups).map(Integer::parseInt).collect(Collectors.toList());

    return solve(data, groups);
  }

  private long solve(String data, List<Integer> groups) {
    final int[] intGroups = groups.stream().mapToInt(Integer::intValue).toArray();
    final Map<CacheKey, Long> cache = new HashMap<>();
    return computeCached(cache, data.toCharArray(), intGroups, 0, 0, 0, false);
  }

  private long computeCached(Map<CacheKey, Long> cache, char[] pattern, int[] groups, final int pos, final int completedGroups, int currentRemaining, boolean requireGap) {
    final CacheKey cacheKey = new CacheKey(pos, completedGroups, currentRemaining, requireGap);
    final Long val = cache.get(cacheKey);
    if (val != null) {
      return val;
    }
    long value = compute(cache, pattern, groups, pos, completedGroups, currentRemaining, requireGap);
    cache.put(cacheKey, value);
    return value;
  }

  private long compute(Map<CacheKey, Long> cache, char[] pattern, int[] groups, int pos, int completedGroups, int currentGroupSize, boolean requireGap) {
    if (completedGroups == groups.length) {
      for (int i = pos; i < pattern.length; i++) {
        if (pattern[i] == '#') {
          return 0;
        }
      }
      return 1;
    }
    if (pos >= pattern.length) {
      return 0;
    }

    final char ch = pattern[pos];

    long sum = 0;
    if (ch != '#') {
      if (requireGap) {
        sum = computeCached(cache, pattern, groups, pos + 1, completedGroups, currentGroupSize, false);
      } else if (currentGroupSize == 0) {
        sum = computeCached(cache, pattern, groups, pos + 1, completedGroups, currentGroupSize, false);
      }
    }
    if (ch != '.') {
      if (!requireGap) {
        int newCurrentGroupSize = currentGroupSize + 1;
        if (newCurrentGroupSize == groups[completedGroups]) {
          sum += computeCached(cache, pattern, groups, pos + 1, completedGroups + 1, 0, true);
        } else {
          sum += computeCached(cache, pattern, groups, pos + 1, completedGroups, newCurrentGroupSize, false);
        }
      }
    }
    return sum;
  }

  private static class CacheKey {
    private final int dataPosition;
    private final int completedExpected;
    private final int progressCurrent;
    private final boolean requireGap;

    public CacheKey(int dataPosition, int completedExpected, int progressCurrent, boolean requireGap) {
      this.dataPosition = dataPosition;
      this.completedExpected = completedExpected;
      this.progressCurrent = progressCurrent;
      this.requireGap = requireGap;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CacheKey cacheKey = (CacheKey) o;
      return dataPosition == cacheKey.dataPosition && completedExpected == cacheKey.completedExpected && progressCurrent == cacheKey.progressCurrent && requireGap == cacheKey.requireGap;
    }

    @Override
    public int hashCode() {
      return Objects.hash(dataPosition, completedExpected, progressCurrent, requireGap);
    }
  }
}
