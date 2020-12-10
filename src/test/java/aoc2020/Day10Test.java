package aoc2020;

import org.junit.Test;
import util.Util;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Day10Test {

  @Test
  public void testPart1() {
    assertEquals(1755, solvePart1("2020/day10.in"));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(220, solvePart1("2020/day10-sample.in"));
  }

  @Test
  public void testPart2() {
    assertEquals(4049565169664L, solvePart2("2020/day10.in"));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(19208, solvePart2("2020/day10-sample.in"));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);

    List<Integer> ints = input.stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
    ints.add(0);
    ints.sort(Comparator.naturalOrder());

    int ones = 0;
    int threes = 1;

    for (int i = 1; i < ints.size(); i++) {
      int prev = ints.get(i - 1);
      int cur = ints.get(i);
      int diff = cur - prev;
      if (diff > 3) {
        throw new RuntimeException();
      }
      if (diff == 1) {
        ones++;
      }
      if (diff == 3) {
        threes++;
      }
    }
    return ones * threes;
  }


  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);

    List<Integer> ints = input.stream().mapToInt(Integer::parseInt).boxed()
            .sorted(Comparator.naturalOrder())
            .collect(Collectors.toList());
    int max = ints.get(ints.size() - 1) + 3;
    ints.add(max);

    long[] sums = new long[max + 1];
    sums[0] = 1;

    for (int val : ints) {
      long x = 0;
      if (val - 1 >= 0) {
        x += sums[val - 1];
      }
      if (val - 2 >= 0) {
        x += sums[val - 2];
      }
      if (val - 3 >= 0) {
        x += sums[val - 3];
      }
      sums[val] = x;
    }
    return sums[max];
  }

}