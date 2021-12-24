package aoc2021;

import util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day24 implements Day {


  private final List<String> input;

  public Day24(List<String> input) {
    this.input = input.stream()
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
  }

  public static Day24 fromResource(String name) {
    return new Day24(Util.readResource(name));
  }


  @Override
  public long solvePart1() {
    List<Integer> digits = IntStream.range(1, 10).boxed().collect(Collectors.toList());
    Collections.reverse(digits);
    return solve(digits);
  }

  @Override
  public long solvePart2() {
    List<Integer> digits = IntStream.range(1, 10).boxed().collect(Collectors.toList());
    return solve(digits);
  }

  private long solve(List<Integer> digits) {
    final List<List<String>> list = input.stream().collect(Util.splitBy(s -> s.startsWith("inp")));
    final Params[] params = list.stream()
            .map(Params::new)
            .collect(Collectors.toList())
            .toArray(new Params[0]);

    List<Set<Long>> targetList = new ArrayList<>();
    Set<Long> targets = new HashSet<>();
    targets.add(0L);
    targetList.add(targets);
    for (int i = params.length - 1; i >= 0; i--) {
      final Params param = params[i];
      final Set<Long> newZ = new HashSet<>();
      for (int digit : digits) {
        param.reverseCand(newZ, digit, targets);
      }
      targets = newZ;
      targetList.add(newZ);
    }
    Collections.reverse(targetList);

    StringBuilder sb = new StringBuilder();
    long z = 0;
    for (int i = 0; i < params.length; i++) {
      final Params param = params[i];
      if (i == params.length - 1) {
        targets = targetList.get(0);
      } else {
        targets = targetList.get(i + 1);
      }
      boolean ok = false;
      for (int digit : digits) {
        long z2 = param.eval(digit, z);
        if (targets.contains(z2)) {
          sb.append(digit);
          z = z2;
          ok = true;
          break;
        }
      }
      if (!ok) {
        throw new RuntimeException();
      }
    }
    return Long.parseLong(sb.toString());
  }

  private class Params {
    final int divisor;
    final int add1;
    final int add2;

    public Params(List<String> strings) {
      divisor = Integer.parseInt(strings.get(3).split(" ")[2]);
      add1 = Integer.parseInt(strings.get(4).split(" ")[2]);
      add2 = Integer.parseInt(strings.get(14).split(" ")[2]);
    }

    long eval(final int digit, final long z) {
      final long z2 = z / divisor;
      if (digit == z % 26 + add1) {
        return z2;
      } else {
        return 26 * z2 + digit + add2;
      }
    }

    void reverseCand(Set<Long> newZ, int digit, Set<Long> targets) {
      for (long target : targets) {
        final long eqZ = target * divisor;
        final long neqZ = ((target - add2 - digit) / 26) * divisor;
        for (int i = 0; i <= 26; i++) {
          maybeAdd(newZ, digit, eqZ + i, targets);
          maybeAdd(newZ, digit, neqZ + i, targets);
        }
      }
    }

    private void maybeAdd(Set<Long> newZ, int digit, long z, Set<Long> targets) {
      if (targets.contains(eval(digit, z))) {
        newZ.add(z);
      }
    }
  }
}