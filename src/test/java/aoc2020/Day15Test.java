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
    State[] states = new State[max + 1];

    int turn = 1;
    for (; turn <= ints.size(); turn++) {
      states[ints.get(turn - 1)] = new State().update(turn);
    }
    int last = ints.get(ints.size() - 1);
    State state = get(states, last);
    for (; turn <= max; turn++) {
      last = state.last();
      state = get(states, last).update(turn);
    }
    return last;
  }

  private State get(State[] states, int index){
    State state = states[index];
    if (state != null) {
      return state;
    }
    state = new State();
    states[index] = state;
    return state;
  }

  private static class State {
    int oldest = -1;
    int newest = -1;

    State update(int turn) {
      oldest = newest;
      newest = turn;
      return this;
    }

    int last() {
      if (oldest != -1) {
        return newest - oldest;
      } else {
        return 0;
      }
    }
  }
}
