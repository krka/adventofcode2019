package aoc2018;

import org.junit.Test;
import util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Day4Test {

  private static final List<String> input = Util.readResource("2018/day4.in");

  @Test
  public void testPart1() {
    assertEquals(109659, solvePart1());
  }

  private long solvePart1() {

    Map<Integer, List<List<String>>> groups = input.stream().sorted().collect(Util.toPartitions((a, b) -> !b.endsWith("begins shift"))).stream()
            .collect(Collectors.groupingBy(this::groupId, Collectors.toList()));

    int maxSleep = groups.values().stream().mapToInt(this::countSleep2).max().getAsInt();

    Map.Entry<Integer, List<List<String>>> best = groups.entrySet().stream()
            .filter(e -> countSleep2(e.getValue()) == maxSleep)
            .reduce(Util.exactlyOne())
            .get();

    int id = best.getKey();
    List<List<String>> bestGroup = best.getValue();

    int[] count = new int[60];
    bestGroup.stream().mapToInt(day -> updateCount(day, count)).sum();
    int bestMinute = -1;
    int bestCount = -1;
    for (int i = 0; i < 60; i++) {
      if (count[i] > bestCount) {
        bestCount = count[i];
        bestMinute = i;
      }
    }
    return id * bestMinute;
  }

  private int updateCount(List<String> day, int[] count) {
    int minute = 0;
    boolean awake = true;
    for (String s : day) {
      int curMinute = Integer.parseInt(s.split("[:\\]]+")[1]);
      if (s.contains("falls asleep")) {
        if (!awake) {
          throw new RuntimeException();
        }
        awake = false;
        minute = curMinute;
      } else if (s.contains("wakes up")) {
        if (awake) {
          throw new RuntimeException();
        }
        awake = true;
        for (int i = minute; i < curMinute; i++) {
          count[i]++;
        }
      }
    }
    if (!awake) {
      for (int i = minute; i < 60; i++) {
        count[i]++;
      }
    }
    return 0;
  }

  private int countSleep2(List<List<String>> group) {
    return group.stream().mapToInt(this::countSleep).sum();
  }

  private int groupId(List<String> group) {
    String s = group.get(0);
    String[] split = s.split("[# ]+");
    return Integer.parseInt(split[3]);
  }

  private int countSleep(List<String> value) {
    int minute = 0;
    int sum = 0;
    boolean awake = true;
    for (String s : value) {
      int curMinute = Integer.parseInt(s.split("[:\\]]+")[1]);
      if (s.contains("falls asleep")) {
        if (!awake) {
          throw new RuntimeException();
        }
        awake = false;
        minute = curMinute;
      } else if (s.contains("wakes up")) {
        if (awake) {
          throw new RuntimeException();
        }
        awake = true;
        sum += (curMinute - minute);
      }
    }
    if (!awake) {
      sum += 60 - minute;
    }
    return sum;
  }

  @Test
  public void testPart2() {
    assertEquals(36371, solvePart2());
  }

  private long solvePart2() {
    Map<Integer, List<List<String>>> groups = input.stream().sorted().collect(Util.toPartitions((a, b) -> !b.endsWith("begins shift"))).stream()
            .collect(Collectors.groupingBy(this::groupId, Collectors.toList()));

    Map<Integer, int[]> guardMinutes = new HashMap<>();
    groups.forEach((id, groups2) -> {
      int[] count = guardMinutes.computeIfAbsent(id, ignore -> new int[60]);
      for (List<String> day : groups2) {
        updateCount(day, count);
      }
    });
    int maxCount = guardMinutes.values().stream().flatMapToInt(value -> IntStream.of(value).max().stream()).max().getAsInt();

    for (Map.Entry<Integer, int[]> entry : guardMinutes.entrySet()) {
      Integer id = entry.getKey();
      int[] count = entry.getValue();
      for (int i = 0; i < 60; i++) {
        if (count[i] == maxCount) {
          return id * i;
        }
      }
    }
    throw new RuntimeException();
  }

}