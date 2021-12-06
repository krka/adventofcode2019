package aoc2021;

import util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day6 {

  private final List<Integer> ints;

  public Day6(String name) {
    ints = Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .map(s -> s.split(","))
            .flatMap(Arrays::stream)
            .map(Integer::parseInt)
            .collect(Collectors.toList());
  }

  public long solvePart1() {
    return solve(80);
  }

  public long solvePart2() {
    return solve(256);
  }

  private long solve(int days) {
    long[] nums = new long[9];
    ints.forEach(i -> nums[i]++);

    for (int day = 0; day < days; day++) {
      long zero = nums[0];
      for (int i = 0; i < 8; i++) {
        nums[i] = nums[i+1];
      }
      nums[8] = zero;
      nums[6] += zero;
    }
    return Arrays.stream(nums).sum();
  }

}
