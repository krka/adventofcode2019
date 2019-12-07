import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7 {

  public static void main(String[] args) {
    System.out.println(new Day7("day7-sample1.in").solve());
    System.out.println(new Day7("day7-sample2.in").solve());
    System.out.println(new Day7("day7-sample3.in").solve());
    System.out.println(new Day7("day7.in").solve());
  }

  private final String filename;

  public Day7(String filename) {
    this.filename = filename;
  }

  private int solve() {
    return Permutation.of(IntStream.range(0, 5).boxed().collect(Collectors.toList()))
            .mapToInt(this::evaluate).max().getAsInt();
  }

  private int evaluate(List<Integer> phases) {
    int value = 0;
    for (Integer phase : phases) {
      IntCode.ListStdout stdout = new IntCode.ListStdout();
      IntCode intCode = IntCode.fromFile(filename, stdout, IntCode.ListStdin.of(phase, value));
      intCode.run();
      List<Integer> list = stdout.getList();
      if (list.size() != 1) {
        throw new RuntimeException("Unexpected list: " + list + " for phase " + phase + " of " + phases);
      }
      value = list.get(0);
    }
    return value;
  }
}
