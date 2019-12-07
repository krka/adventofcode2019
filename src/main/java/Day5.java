import java.util.List;

public class Day5 {
  static List<Integer> part1() throws Exception {
    IntCode vm = IntCode.fromResource("day5.in");
    vm.writeStdin(1);
    vm.run();
    return vm.drainStdout();
  }

  static List<Integer> part2() throws Exception {
    IntCode vm = IntCode.fromResource("day5.in");
    vm.writeStdin(5);
    vm.run();
    return vm.drainStdout();
  }
}
