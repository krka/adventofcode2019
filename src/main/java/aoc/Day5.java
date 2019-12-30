package aoc;

import intcode.IntCode;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day5 {
  public static List<BigInteger> part1() {
    IntCode vm = IntCode.fromResource("day5.in");
    vm.writeStdin(1);
    vm.run();
    assertEquals(IntCode.State.HALTED, vm.getState());
    return vm.drainStdout();
  }

  public static List<BigInteger> part2() {
    IntCode vm = IntCode.fromResource("day5.in");
    vm.writeStdin(5);
    vm.run();
    assertEquals(IntCode.State.HALTED, vm.getState());
    return vm.drainStdout();
  }
}
