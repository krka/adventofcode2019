package aoc2022;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Day20 implements Day {

  private final List<Integer> input;

  public Day20(String name) {
    input = Util.readResource(name).stream().filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .collect(Collectors.toList());
  }


  @Override
  public long solvePart1() {
    return solve(1, 1);
  }

  @Override
  public long solvePart2() {
    return solve(811589153L, 10);
  }

  private long solve(long factor, int rounds) {
    List<W> input2 = input.stream().map(v -> new W(v * factor)).collect(Collectors.toList());
    W theZero = input2.stream().filter(w -> w.value == 0).findAny().get();

    ArrayList<W> copy = new ArrayList<>(input2);
    for (int i = 0; i < rounds; i++) {
      mix(copy, input2);
    }

    int zeroIndex = copy.indexOf(theZero);
    final long a = copy.get((zeroIndex + 1000) % input.size()).value;
    final long b = copy.get((zeroIndex + 2000) % input.size()).value;
    final long c = copy.get((zeroIndex + 3000) % input.size()).value;
    return a + b + c;
  }

  private void mix(List<W> copy, List<W> order) {
    final long size = copy.size() - 1;
    for (W w : order) {
      int position = copy.indexOf(w);
      copy.remove(position);
      position = (int) Math.floorMod(position + w.value, size);
      copy.add(position, w);
    }
  }

  private static class W {
    final long value;

    public W(long value) {
      this.value = value;
    }
  }
}
