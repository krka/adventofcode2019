package aoc2019;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 {
  static long part1(int start, int end) {
    return IntStream.rangeClosed(start, end)
            .mapToObj(Day4::to6DigitString)
            .filter(Day4::inOrder)
            .filter(n -> hasGroup(n, i -> i >= 2))
            .count();
  }


  static long part2(int start, int end) {
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
