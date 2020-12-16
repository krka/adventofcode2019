package aoc2020;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Day15Test {

  @Test
  public void testPart1() {
    assertEquals(1111, solvePart1("20,9,11,0,1,2"));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(436, solvePart1("0,3,6"));
    assertEquals(1, solvePart1("1,3,2"));
    assertEquals(10, solvePart1("2,1,3"));
    assertEquals(27, solvePart1("1,2,3"));
  }

  @Test
  public void testPart2() {
    assertEquals(48568, solvePart2("20,9,11,0,1,2"));
  }

  private long solvePart1(String nums) {
    return solve(nums, 2020);
  }

  private long solvePart2(String nums) {
    return solve(nums, 30000000);
  }

  private long solve(String nums, int max) {
    List<Integer> ints = Stream.of(nums.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    int[] states = new int[max];
    for (int i = 0; i < max; i++) {
      states[i] = -1;
    }
    int turn = 1;
    int last = -1;
    for (; turn <= ints.size(); turn++) {
      last = updateAndLast(states, ints.get(turn - 1), turn);
    }
    for (; turn < max; turn++) {
      last = updateAndLast(states, last, turn);
    }
    return last;
  }

  static int updateAndLast(int[] states, int index, int turn) {
    int prev = states[index];
    states[index] = turn;
    if (prev != -1) {
      return turn - prev;
    } else {
      return 0;
    }
  }
}
