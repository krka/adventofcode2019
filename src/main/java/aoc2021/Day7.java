package aoc2021;

import util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day7 {

  private final List<Integer> input;
  private final int max;
  private final int min;

  public Day7(List<Integer> input) {
    this.input = input;
    input.sort(Integer::compareTo);
    min = input.get(0);
    max = input.get(input.size() - 1);
  }

  public Day7(String name) {
    this(Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .map(s -> s.split(","))
            .flatMap(Arrays::stream)
            .map(Integer::parseInt)
            .sorted()
            .collect(Collectors.toList()));
  }

  public long solvePart1() {
    int median = input.get(input.size() / 2);
    return input.stream().mapToLong(x -> Math.abs(x - median)).sum();
  }

  public long solvePart2() {
    long sum = input.stream().mapToLong(Integer::longValue).sum();
    long average = (int) (sum / input.size());
    return LongStream.range(average - 1, average + 2)
            .map(i -> input.stream().mapToLong(x -> steps(i, x)).sum())
            .min().getAsLong();
  }

  private long steps(long a, long b) {
    long steps = Math.abs(b - a);
    return steps * (steps + 1) / 2;
  }
}
