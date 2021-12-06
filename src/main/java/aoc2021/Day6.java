package aoc2021;

import util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6 {

  private final List<Integer> input;

  public Day6(String name) {
    input = Util.readResource(name).stream()
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
    input.forEach(i -> nums[i]++);
    IntStream.range(0, days).forEach(day -> {
      long zero = nums[0];
      System.arraycopy(nums, 1, nums, 0, 8);
      nums[8] = zero;
      nums[6] += zero;
    });
    return Arrays.stream(nums).sum();
  }

}
