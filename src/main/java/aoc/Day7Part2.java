package aoc;

import intcode.IntCode;

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
            .mapToInt(this::evaluate).max().getAsInt();
  }

  static boolean firstRun = true;

  private int evaluate(List<Integer> phases) {
    IntCode[] vms = new IntCode[5];
    for (int i = 0; i < 5; i++) {
      vms[i] = IntCode.fromResource(name);
      vms[i].writeStdin(phases.get(i));
    }
    int prev = 0;
    while (true) {
      for (int i = 0; i < 5; i++) {
        IntCode vm = vms[i];
        vm.writeStdin(prev);
        vm.run();
        Integer value = vm.getStdout().poll();
        if (value == null) {
          for (int j = 0; j < 5; j++) {
            if (IntCode.State.HALTED != vms[j].getState()) {
              throw new RuntimeException("Unexpected state: " + vms[j].getState());
            }
            if (firstRun) {
              vms[j].printAnalysis("day7-part2-vm-" + j + ".txt");
            }
          }
          firstRun = false;
          return prev;
        } else {
          prev = value;
        }
      }
    }
  }
}
