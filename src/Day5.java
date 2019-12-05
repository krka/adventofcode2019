import java.util.Arrays;

public class Day5 {
  public static void main(String[] args) throws Exception {
    part1();
    part2();
  }

  private static void part1() throws Exception {
    IntCode.ListStdout stdout = new IntCode.ListStdout();
    IntCode.ListStdin stdin = new IntCode.ListStdin(Arrays.asList(1));
    IntCode vm = IntCode.fromFile("day5.in", stdout, stdin);
    vm.run();
    System.out.println("Part 1: " + stdout.getList());
  }

  private static void part2() throws Exception {
    IntCode.ListStdout stdout = new IntCode.ListStdout();
    IntCode.ListStdin stdin = new IntCode.ListStdin(Arrays.asList(5));
    IntCode vm = IntCode.fromFile("day5.in", stdout, stdin);
    vm.run();
    System.out.println("Part 2: " + stdout.getList());
  }
}
