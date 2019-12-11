package aoc;

import intcode.IntCode;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7 {
  private final String name;

  public Day7(String name) {
    this.name = name;
  }

  int solve() {
    return Permutation.of(IntStream.range(0, 5).boxed().collect(Collectors.toList()))
            .map(this::evaluate).max(BigInteger::compareTo).get().intValueExact();
  }

  private BigInteger evaluate(List<Integer> phases) {
    BigInteger value = BigInteger.ZERO;
    for (Integer phase : phases) {
      IntCode intCode = IntCode.fromResource(name);
      intCode.writeStdin(phase);
      intCode.writeStdin(value);
      intCode.run();
      List<BigInteger> list = intCode.drainStdout();
      if (list.size() != 1) {
        throw new RuntimeException("Unexpected list: " + list + " for phase " + phase + " of " + phases);
      }
      value = list.get(0);
    }
    return value;
  }
}
