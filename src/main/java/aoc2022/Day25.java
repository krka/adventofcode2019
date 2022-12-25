package aoc2022;

import util.DayS;
import util.Pair;
import util.Util;

import java.util.ArrayList;
import java.util.List;

public class Day25 implements DayS {

  private final List<String> input;

  public Day25(String name) {
    final List<List<String>> collect = Util.readResource(name).stream().collect(Util.splitBy(s -> s.isEmpty()));
    input = collect.get(0);
  }

  private long fromSnafu(String s) {
    long res = 0;
    int i = 0;
    while (i < s.length()) {
      final char c = s.charAt(i);
      switch (c) {
        case '0': res = 5 * res + 0; break;
        case '1': res = 5 * res + 1; break;
        case '2': res = 5 * res + 2; break;
        case '-': res = 5 * res - 1; break;
        case '=': res = 5 * res - 2; break;
        default: throw new RuntimeException();
      }
      i++;
    }
    return res;
  }

  private String toSnafu(long x) {
    StringBuilder sb = new StringBuilder();
    while (x > 0) {
      final int digit = (int) (x % 5);
      x /= 5;
      switch (digit) {
        case 0: sb.append('0'); break;
        case 1: sb.append('1'); break;
        case 2: sb.append('2'); break;
        case 3: sb.append('='); x++; break;
        case 4: sb.append('-'); x++; break;
        default: throw new RuntimeException();
      }
    }
    return sb.reverse().toString();
  }


  @Override
  public String solvePart1() {
    return toSnafu(input.stream().mapToLong(this::fromSnafu).sum());
  }

  @Override
  public String solvePart2() {
    return "";
  }
}

