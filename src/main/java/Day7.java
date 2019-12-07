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
            .mapToInt(this::evaluate).max().getAsInt();
  }

  private int evaluate(List<Integer> phases) {
    int value = 0;
    for (Integer phase : phases) {
      IntCode intCode = IntCode.fromResource(name);
      intCode.writeStdin(phase);
      intCode.writeStdin(value);
      intCode.run();
      List<Integer> list = intCode.drainStdout();
      if (list.size() != 1) {
        throw new RuntimeException("Unexpected list: " + list + " for phase " + phase + " of " + phases);
      }
      value = list.get(0);
    }
    return value;
  }
}
