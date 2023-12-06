package aoc2023;

import util.Day;
import util.Interval;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day6 implements Day {

  private final List<List<String>> lines;

  public Day6(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
  }

  @Override
  public long solvePart1() {
    long ans = 1;
    final String[] parts1 = lines.get(0).get(0).split(" +");
    final String[] parts2 = lines.get(0).get(1).split(" +");
    for (int i = 1; i < parts1.length; i++) {
      final int time = Integer.parseInt(parts1[i]);
      final int bestDistance = Integer.parseInt(parts2[i]);
      ans *= solve(time, bestDistance);
    }
    return ans;
  }

  private long solve(long time, long bestDistance) {
    long count = 0;
    for (long i = 1; i <= time; i++) {
      long speed = i;
      long remaining = time - i;
      long distance = remaining * speed;
      if (distance > bestDistance) {
        count++;
      }
    }
    return count;
  }

  @Override
  public long solvePart2() {
    long ans = 1;
    final String[] parts1 = lines.get(0).get(0).replace(" ", "").split(":");
    final String[] parts2 = lines.get(0).get(1).replace(" ", "").split(":");

    final long time = Long.parseLong(parts1[1]);
    final long bestDistance = Long.parseLong(parts2[1]);
    ans *= solve(time, bestDistance);
    return ans;
  }

}
