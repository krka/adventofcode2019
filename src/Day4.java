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
            .mapToObj(n1 -> String.format("%06d", n1))
            .filter(Day4::validPart1)
            .count();
  }

  private static long part2(int start, int end) {
    return IntStream.rangeClosed(start, end)
            .mapToObj(n -> String.format("%06d", n))
            .filter(Day4::validPart2)
            .count();
  }

  private static boolean validPart1(String n) {
    char prev = 0;
    boolean hasDouble = false;
    for (char ch : n.toCharArray()) {
      if (prev == ch) {
        hasDouble = true;
      }
      if (prev > ch) {
        return false;
      }
      prev = ch;
    }
    return hasDouble;
  }

  private static boolean validPart2(String n) {
    char prev = 0;
    int repeats = 1;
    boolean hasDouble = false;
    for (char ch : n.toCharArray()) {
      if (prev == ch) {
        repeats++;
      } else {
        hasDouble |= repeats == 2;
        repeats = 1;
      }
      if (prev > ch) {
        return false;
      }
      prev = ch;
    }
    hasDouble |= repeats == 2;
    return hasDouble;
  }
}
