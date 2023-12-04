package aoc2023;

import util.Day;
import util.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Character.isDigit;

public class Day4 implements Day {

  private final List<String> lines;

  public Day4(String name) {
    this.lines = Util.readResource(name).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
  }

  @Override
  public long solvePart1() {
    return lines.stream().mapToLong(line -> {
      final int hits = getHits(line);
      if (hits == 0) {
        return 0;
      } else {
        return 1L << (hits - 1);
      }
      }).sum();
  }

  @Override
  public long solvePart2() {
    long[] count = new long[lines.size()];
    Arrays.fill(count, 1);
    int i = 0;
    for (String line : lines) {
      final int hits = getHits(line);
      long curCount = count[i];
      for (int j = 1; j <= hits; j++) {
        count[i + j] += curCount;
      }
      i++;
    }
    return Arrays.stream(count).sum();
  }

  private static int getHits(String line) {
    final String[] strings = line.split("[ :]+");
    Set<Integer> winning = new HashSet<>();
    int hits = 0;
    boolean mode = false;
    for (int i = 2; i < strings.length; i++) {
      final String string = strings[i].trim();
      if (string.equals("|")) {
        mode = true;
      } else {
        int value = Integer.parseInt(string);
        if (mode) {
          if (winning.contains(value)) {
            hits++;
          }
        } else {
          winning.add(value);
        }
      }
    }
    return hits;
  }
}
