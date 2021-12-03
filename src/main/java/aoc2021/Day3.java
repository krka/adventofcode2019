package aoc2021;

import util.Util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day3 {

  private final List<String> input;
  private final int majority;

  public Day3(String name) {
    input = Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    majority = (input.size()+1)/2;
  }

  public long solvePart1() {
    List<Integer> countOnes = input.stream()
            .map(s -> s.codePoints().map(x -> x - '0').boxed().collect(Collectors.toList()))
            .reduce((a, b) -> Util.zip(a, b).stream()
                    .mapToInt(pair -> pair.a() + pair.b()).boxed()
                    .collect(Collectors.toList()))
            .get();
    int gamma = fromBin(toBinString(countOnes, c -> c >= majority ? "1" : "0"));
    int epsilon = fromBin(toBinString(countOnes, c -> c < majority ? "1" : "0"));
    return gamma * epsilon;
  }

  public long solvePart2() {
    List<String> sorted = input.stream().sorted().collect(Collectors.toList());
    int oxygen = binarySearch(sorted, true);
    int scrubber = binarySearch(sorted, false);
    return oxygen * scrubber;
  }

  private int binarySearch(List<String> input, boolean majority) {
    StringBuilder prefix = new StringBuilder();
    while (input.size() > 1) {
      int index = Collections.binarySearch(input, prefix + "1");
      if (index < 0) {
        index = -1 - index;
      }
      int numberOfOnes = input.size() - index;

      if ((numberOfOnes >= index) == majority) {
        input = input.subList(index, input.size());
        prefix.append("1");
      } else {
        input = input.subList(0, index);
        prefix.append("0");
      }
    }
    return fromBin(input.get(0));
  }

  private static int fromBin(String s) {
    return Integer.parseInt(s, 2);
  }

  private static String toBinString(List<Integer> countOnes, Function<Integer, String> integerStringFunction) {
    return countOnes.stream().map(integerStringFunction).collect(Collectors.joining());
  }
}
