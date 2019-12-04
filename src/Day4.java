import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 {
  public static void main(String[] args) {
    int start = 236491;
    int end = 713787;
    System.out.println("Part 1: " + part1(start, end));
    System.out.println("Part 2: " + part2(start, end));
  }

  private static long part1(int start, int end) {
    return IntStream.rangeClosed(start, end)
            .mapToObj(Day4::to6DigitString)
            .filter(Day4::inOrder)
            .filter(n -> hasGroup(n, i -> i >= 2))
            .count();
  }


  private static long part2(int start, int end) {
    return IntStream.rangeClosed(start, end)
            .mapToObj(Day4::to6DigitString)
            .filter(Day4::inOrder)
            .filter(n -> hasGroup(n, i -> i == 2))
            .count();
  }

  private static boolean hasGroup(String n, Predicate<Integer> predicate) {
    return n.chars().asLongStream().boxed()
            .collect(Collectors.groupingBy(Function.identity()))
            .values().stream()
            .map(List::size)
            .anyMatch(predicate);
  }

  private static String to6DigitString(int n) {
    return String.format("%06d", n);
  }

  private static boolean inOrder(String n) {
    return n.chars().mapToObj(Character::toString).sorted().collect(Collectors.joining()).equals(n);
  }
}
