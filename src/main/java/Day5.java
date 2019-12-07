import java.util.Arrays;
import java.util.List;

public class Day5 {
  static List<Integer> part1() throws Exception {
    IntCode.ListStdout stdout = new IntCode.ListStdout();
    IntCode.ListStdin stdin = new IntCode.ListStdin(Arrays.asList(1));
    IntCode vm = IntCode.fromResource("day5.in", stdout, stdin);
    vm.run();
    return stdout.getList();
  }

  static List<Integer> part2() throws Exception {
    IntCode.ListStdout stdout = new IntCode.ListStdout();
    IntCode.ListStdin stdin = new IntCode.ListStdin(Arrays.asList(5));
    IntCode vm = IntCode.fromResource("day5.in", stdout, stdin);
    vm.run();
    return stdout.getList();
  }
}
