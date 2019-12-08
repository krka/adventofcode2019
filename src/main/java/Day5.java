import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day5 {
  static List<Integer> part1() {
    IntCode vm = IntCode.fromResource("day5.in");
    vm.writeStdin(1);
    vm.run();
    assertEquals(IntCode.State.HALTED, vm.getState());
    vm.printAnalysis("day5-part1.txt");
    return vm.drainStdout();
  }

  static List<Integer> part2() {
    IntCode vm = IntCode.fromResource("day5.in");
    vm.writeStdin(5);
    vm.run();
    assertEquals(IntCode.State.HALTED, vm.getState());
    vm.printAnalysis("day5-part2.txt");
    return vm.drainStdout();
  }
}
