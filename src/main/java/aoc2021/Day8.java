package aoc2021;

import util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day8 {

  private final List<String> input;

  public Day8(String name) {
    this.input = Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
  }

  public long solvePart1() {
    return input.stream().mapToLong(Day8::calc).sum();
  }

  private static long calc(String s) {
    String[] split = s.split("\\|");
    String right = split[1];
    return Arrays.stream(right.split(" "))
            .mapToLong(String::length)
            .filter(n -> n == 2 || n == 3 || n==4 || n==7)
                    .count();
  }

  public long solvePart2() {
    return input.stream().mapToLong(Day8::calc2).sum();
  }

  private static long calc2(String s) {
    /*
       aaa
      b   c
      b   c
      b   c
       ddd
      e   f
      e   f
      e   f
       ggg
     */
    String[] split = s.split("\\|");
    String leftString = split[0];
    String rightString = split[1];

    List<String> left = Arrays.stream(leftString.split(" "))
            .map(String::strip)
            .filter(s2 -> !s2.isEmpty())
            .collect(Collectors.toList());

    Set<Character> one = toSet(left.stream().filter(x -> x.length() == 2).findAny().get());
    Set<Character> seven = toSet(left.stream().filter(x -> x.length() == 3).findAny().get());
    Set<Character> four = toSet(left.stream().filter(x -> x.length() == 4).findAny().get());
    char segmentA = Util.diff(seven, one).stream().reduce(Util.exactlyOne()).get();
    int[] counts = new int[7];
    for (char c : leftString.toCharArray()) {
      if (Character.isAlphabetic(c)) {
        counts[c - 'a']++;
      }
    }
    char segmentB = 0;
    char segmentC = 0;
    char segmentD = 0;
    char segmentE = 0;
    char segmentF = 0;
    char segmentG = 0;
    int sumCount = 0;
    for (int i = 0; i < 7; i++) {
      char candidate = (char) ('a' + i);
      sumCount += counts[i];
      switch (counts[i]) {
        case 4: segmentE = candidate; break;
        case 6: segmentB = candidate; break;
        case 7: {
          if (four.contains(candidate)) {
            segmentD = candidate;
          } else {
            segmentG = candidate;
          }
          break;
        }
        case 8: {
          if (candidate != segmentA) {
            segmentC = candidate;
          }
          break;
        }
        case 9: segmentF = candidate; break;
        default:
      }
    }
    if (sumCount != 49) {
      throw new RuntimeException("Failed: " + Arrays.toString(counts) + ": " + leftString);
    }
    String right = rightString
            .replace(segmentA, 'A')
            .replace(segmentB, 'B')
            .replace(segmentC, 'C')
            .replace(segmentD, 'D')
            .replace(segmentE, 'E')
            .replace(segmentF, 'F')
            .replace(segmentG, 'G');

    int res = 0;
    for (String part : right.split(" ")) {
      if (!part.isEmpty()) {
        res *= 10;
        res += toDigit(part);
      }
    }

    return res;
  }

  private static int toDigit(String s) {
    String digit = s.codePoints().mapToObj(x -> "" + (char) x).sorted().collect(Collectors.joining());
    switch (digit) {
      case "ABCEFG": return 0;
      case "CF": return 1;
      case "ACDEG": return 2;
      case "ACDFG": return 3;
      case "BCDF": return 4;
      case "ABDFG": return 5;
      case "ABDEFG": return 6;
      case "ACF": return 7;
      case "ABCDEFG": return 8;
      case "ABCDFG": return 9;
      default: throw new RuntimeException("Can't parse: " + digit);
    }
  }

  private static Set<Character> toSet(String s) {
    return s.codePoints().mapToObj(x -> (char) x).collect(Collectors.toSet());
  }

  private static List<String> toList(String s) {
    return Arrays.stream(s.split(" ")).map(String::strip).filter(s2 -> !s2.isEmpty()).collect(Collectors.toList());
  }
}
