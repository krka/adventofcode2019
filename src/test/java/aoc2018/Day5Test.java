package aoc2018;

import org.junit.Test;
import util.Util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class Day5Test {

  private static final List<String> input = Util.readResource("2018/day5.in");

  @Test
  public void testPart1() {
    assertEquals(11264, solvePart1());
  }

  private long solvePart1() {
    String line = input.get(0);
    return reduce(line.chars().toArray()).length;
  }

  private int[] reduce(int[] chars) {
    boolean run = true;
    while (run) {
      run = false;

      int len = chars.length;

      for (int i = 0; i < len - 1; i++) {
        if (match(chars[i], chars[i + 1])) {
          chars[i] = 0;
          chars[i + 1] = 0;
          run = true;
        }
      }
      if (run) {
        chars = IntStream.of(chars).filter(value -> value != 0).toArray();
      }
    }
    return chars;
  }

  private boolean match(int left, int right) {
    return Math.abs(left - right) == 32;
  }

  @Test
  public void testPart2() {
    assertEquals(4552, solvePart2());
  }

  private int solvePart2() {
    String line = input.get(0);
    int[] reduced = reduce(line.chars().toArray());
    int min = Integer.MAX_VALUE;
    for (int i = 'a'; i <= 'z'; i++) {
      int finalI = i;
      min = Math.min(min, reduce(IntStream.of(reduced).filter(value -> value != finalI && value != (finalI - 32)).toArray()).length);
    }
    return min;
  }


}