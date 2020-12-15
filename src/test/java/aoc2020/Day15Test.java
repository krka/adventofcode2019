package aoc2020;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    assertEquals(30000000, solvePart2("20,9,11,0,1,2"));
  }

  private long solvePart1(String nums) {
    return solve(nums, 2020);
  }

  private long solvePart2(String nums) {
    return solve(nums, 30000000);
  }

  private long solve(String nums, int max) {
    List<Integer> ints = Stream.of(nums.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    Map<Integer, List<Integer>> mem = new HashMap<>();
    int turn = 1;
    for (; turn <= ints.size(); turn++) {
      mem.computeIfAbsent(ints.get(turn - 1), ignore -> new ArrayList<>()).add(turn);
    }
    int last = ints.get(ints.size() - 1);
    List<Integer> list = mem.computeIfAbsent(last, ignore -> new ArrayList<>());
    while (turn <= max) {
      if (list.size() >= 2) {
        last = list.get(1) - list.get(0);
      } else {
        last = 0;
      }
      list = mem.computeIfAbsent(last, ignore -> new ArrayList<>());
      list.add(turn);
      while (list.size() > 2) {
        list.remove(0);
      }
      turn++;
    }
    return last;
  }
}
