package aoc2020;

import org.junit.Test;
import util.Util;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Day9Test {

  private static final List<String> input = Util.readResource("2020/day9.in");

  @Test
  public void testPart1() {
    assertEquals(26796446L, solvePart1());
  }

  private long solvePart1() {
    List<Long> ints = input.stream().mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    for (int i = 25; i < ints.size(); i++) {
      long x = ints.get(i);
      if (isTarget(ints, i, x)) {
        return x;
      }
    }
    return -1;
  }

  private boolean isTarget(List<Long> ints, int i, long x) {
    for (int j = 0; j < 24; j++) {
      long a = ints.get(i - j - 1);
      for (int k = j + 1; k < 25; k++) {
        long b = ints.get(i - k - 1);
        if (x == a + b) {
          return false;
        }
      }
    }
    return true;
  }

  @Test
  public void testPart2() {
    assertEquals(3353494, solvePart2());
  }

  private long solvePart2() {
    List<Long> ints = input.stream().mapToLong(Long::parseLong).boxed().collect(Collectors.toList());

    int minIndex = 0;
    int maxIndex = 1;
    long sum = ints.get(0) + ints.get(1);
    long target = 26796446L;

    // This only works because all input values are non-negative
    while (sum != target) {
      if (sum < target) {
        maxIndex++;
        sum += ints.get(maxIndex);
      } else {
        sum -= ints.get(minIndex);
        minIndex++;
      }
    }
    long minVal = ints.subList(minIndex, maxIndex + 1).stream().mapToLong(Long::longValue).min().getAsLong();
    long maxVal = ints.subList(minIndex, maxIndex + 1).stream().mapToLong(Long::longValue).max().getAsLong();
    return minVal + maxVal;
  }

}