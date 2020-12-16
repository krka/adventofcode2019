package aoc2020;

import org.junit.Test;
import util.Pair;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class Day16Test {

  public static final String DAY = "16";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(27850, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(71, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(491924517533L, solvePart2(MAIN_INPUT));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    List<List<String>> parts = input.stream().collect(Util.splitBy(s -> s.isEmpty()));
    List<String> ranges = parts.get(0);
    List<String> otherTickets = parts.get(2);

    List<Boolean> rangeMap = new ArrayList<>();
    for (String range : ranges) {
      String[] split1 = range.split(":");
      String[] split = split1[1].trim().split("[ -]+");
      int min = Integer.parseInt(split[0]);
      int max = Integer.parseInt(split[1]);
      int min2 = Integer.parseInt(split[3]);
      int max2 = Integer.parseInt(split[4]);

      for (int i = min; i <= max; i++) {
        setValid(rangeMap, i);
      }
      for (int i = min2; i <= max2; i++) {
        setValid(rangeMap, i);
      }

    }

    int sum = 0;
    for (String s : otherTickets.subList(1, otherTickets.size())) {
      String[] split = s.split(",");
      for (String s1 : split) {
        int val = Integer.parseInt(s1);
        if (val >= rangeMap.size() || !rangeMap.get(val)) {
          sum += val;
        }
      }
    }
    return sum;
  }

  private void setValid(List<Boolean> rangeMap, int i) {
    while (rangeMap.size() <= i) {
      rangeMap.add(false);
    }
    rangeMap.set(i, true);
  }

  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);
    List<List<String>> parts = input.stream().collect(Util.splitBy(String::isEmpty));
    List<String> ranges = parts.get(0);
    String yourTicket = parts.get(1).get(1);
    List<String> otherTickets = parts.get(2).subList(1, parts.get(2).size());

    List<Boolean> rangeBitmap = new ArrayList<>();
    List<Pair<Vec2, Vec2>> rangePairs = new ArrayList<>();
    for (String range : ranges) {
      String[] split1 = range.split(":");
      String[] split = split1[1].trim().split("[ -]+");
      int min = Integer.parseInt(split[0]);
      int max = Integer.parseInt(split[1]);
      int min2 = Integer.parseInt(split[3]);
      int max2 = Integer.parseInt(split[4]);

      rangePairs.add(Pair.of(Vec2.of(min, max), Vec2.of(min2, max2)));

      for (int i = min; i <= max; i++) {
        setValid(rangeBitmap, i);
      }
      for (int i = min2; i <= max2; i++) {
        setValid(rangeBitmap, i);
      }
    }

    int numParts = yourTicket.split(",").length;

    List<Set<Integer>> candidates = new ArrayList<>();
    for (int i = 0; i < ranges.size(); i++) {
      candidates.add(IntStream.range(0, numParts).boxed().collect(Collectors.toSet()));
    }

    Map<Integer, Integer> known = new HashMap<>();
    boolean run = true;
    while (run) {
      run = false;
      scanTicket(rangeBitmap, candidates, rangePairs, yourTicket);
      for (String s : otherTickets) {
        scanTicket(rangeBitmap, candidates, rangePairs, s);
      }
      for (int i = 0; i < candidates.size(); i++) {
        Set<Integer> set = candidates.get(i);
        if (set.size() == 1 && !known.containsKey(i)) {
          int val = set.iterator().next();
          known.put(i, val);
          for (int j = 0; j < candidates.size(); j++) {
            candidates.get(j).remove(val);
          }
          run = true;
        }
      }
    }

    Map<Integer, Integer> rev = Util.reverseMap(known);
    String[] split = yourTicket.split(",");
    long prod = 1;
    for (int i = 0; i < split.length; i++) {
      Integer index = rev.get(i);
      if (index != null && index <= 5) {
        prod *= Long.parseLong(split[i]);
      }
    }
    return prod;
  }

  private void scanTicket(List<Boolean> rangeMap, List<Set<Integer>> map2, List<Pair<Vec2, Vec2>> rangeMap2, String s) {
    String[] split = s.split(",");
    boolean isValid = true;
    for (String s1 : split) {
      int val = Integer.parseInt(s1);
      if (val >= rangeMap.size() || !rangeMap.get(val)) {
        isValid = false;
      }
    }
    if (isValid) {
      for (int pos = 0; pos < split.length; pos++) {
        int val = Integer.parseInt(split[pos]);
        for (int j = 0; j < rangeMap2.size(); j++) {
          Pair<Vec2, Vec2> pair = rangeMap2.get(j);
          if (!isInRange(val, pair)) {
            map2.get(j).remove(pos);
          }
        }
      }
    }
  }

  private boolean isInRange(int val, Pair<Vec2, Vec2> pair) {
    if (val >= pair.a().getX() && val <= pair.a().getY()) {
      return true;
    }
    if (val >= pair.b().getX() && val <= pair.b().getY()) {
      return true;
    }
    return false;
  }
}
