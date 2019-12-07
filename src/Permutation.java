import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Permutation {
  static <T> Stream<List<T>> of(List<T> input) {
    int n = input.size();
    if (n > 10) {
      throw new IllegalStateException("Input is too big!");
    }
    int fac = IntStream.rangeClosed(1, n).reduce(1, (left, right) -> left * right);
    return IntStream.range(0, fac)
            .mapToObj(i -> getPermutation(input, n, i));
  }

  private static <T> List<T> getPermutation(List<T> input, int n, int i) {
    List<T> inputCopy = new ArrayList<>(input);
    List<T> result = new ArrayList<>(n);
    for (int j = n; j > 0; j--) {
      result.add(inputCopy.remove(i % j));
      i /= j;
    }
    return result;
  }

  public static void main(String[] args) {
    of(Arrays.asList(1,2,3)).forEach(System.out::println);
  }

}
