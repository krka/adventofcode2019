package aoc2019;

import intcode.IntCode;
import util.Permutation;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7Part2 {

  private final String name;

  public Day7Part2(String name) {
    this.name = name;
  }

  int solve() {
    return Permutation.of(IntStream.range(5, 10).boxed().collect(Collectors.toList()))
            .map(this::evaluate).max(BigInteger::compareTo).get().intValueExact();
  }

  private BigInteger evaluate(List<Integer> phases) {
    IntCode[] vms = new IntCode[5];
    for (int i = 0; i < 5; i++) {
      vms[i] = IntCode.fromResource(name);
      vms[i].writeStdin(phases.get(i));
    }
    BigInteger prev = BigInteger.ZERO;
    while (true) {
      for (int i = 0; i < 5; i++) {
        IntCode vm = vms[i];
        vm.writeStdin(prev);
        vm.run();
        BigInteger value = vm.getStdout().poll();
        if (value == null) {
          for (int j = 0; j < 5; j++) {
            if (IntCode.State.HALTED != vms[j].getState()) {
              throw new RuntimeException("Unexpected state: " + vms[j].getState());
            }
          }
          return prev;
        } else {
          prev = value;
        }
      }
    }
  }
}
