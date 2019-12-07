import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7Part2 {

  public static void main(String[] args) {
    System.out.println(new Day7Part2("day7.in").solve());
  }

  private final String filename;

  public Day7Part2(String filename) {
    this.filename = filename;
  }

  private int solve() {
    return Permutation.of(IntStream.range(5, 10).boxed().collect(Collectors.toList()))
            .mapToInt(this::evaluate).max().getAsInt();
  }

  private int evaluate(List<Integer> phases) {
    IntcodePipe[] intcodePipes = new IntcodePipe[5];
    for (int i = 0; i < 5; i++) {
      intcodePipes[i] = new IntcodePipe();
      intcodePipes[i].writeValue(phases.get(i));
    }

    intcodePipes[0].writeValue(0);

    IntCode[] vms = new IntCode[5];
    for (int i = 0; i < 5; i++) {
      vms[i] = IntCode.fromFile(filename, intcodePipes[(i + 1) % 5], intcodePipes[i]);
    }

    List<Thread> threads = IntStream.range(0, 5)
            .mapToObj(i -> {
              Thread thread = new Thread(vms[i]);
              thread.start();
              return thread;
            })
            .collect(Collectors.toList());

    threads.forEach(thread -> {
      try {
        thread.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    return intcodePipes[0].readValue();
  }

}
