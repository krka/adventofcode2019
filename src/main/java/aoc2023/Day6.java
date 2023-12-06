package aoc2023;

import util.Day;
import util.Util;

import java.util.List;

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
    final long target = bestDistance + 1;
    final double sqrt = Math.sqrt(time * time - 4 * target);
    long x1 = (long) Math.ceil((time - sqrt) / 2);
    long x2 = (long) Math.floor((time + sqrt) / 2);
    return x2 - x1 + 1;
  }

  @Override
  public long solvePart2() {
    final String[] parts1 = lines.get(0).get(0).replace(" ", "").split(":");
    final String[] parts2 = lines.get(0).get(1).replace(" ", "").split(":");

    final long time = Long.parseLong(parts1[1]);
    final long bestDistance = Long.parseLong(parts2[1]);
    return solve(time, bestDistance);
  }

}
