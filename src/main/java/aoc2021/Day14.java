package aoc2021;

import util.Pair;
import util.Util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Day14 implements Day {

  public static final AtomicLong ONE = new AtomicLong(1);
  private final String start;
  private final Map<Pair<Character, Character>, Character> map;

  public Day14(String name) {
    List<String> input = Util.readResource(name);
    start = input.get(0);
    map = input.stream()
            .skip(2)
            .filter(s -> !s.isEmpty())
            .map(s -> s.split(" -> "))
            .collect(Collectors.toMap(x -> Pair.of(x[0].charAt(0), x[0].charAt(1)), x -> x[1].charAt(0)));
  }

  public long solvePart1() {
    return solve(10);
  }

  public long solvePart2() {
    return solve(40);
  }

  private long solve(int steps) {
    Map<Pair<Character, Character>, AtomicLong> pairCounts = new HashMap<>();
    char prev = 0;
    for (char c : start.toCharArray()) {
      if (prev != 0) {
        inc(pairCounts, Pair.of(prev, c), ONE);
      }
      prev = c;
    }
    inc(pairCounts, Pair.of(prev, '0'), ONE);

    for (int i = 0; i < steps; i++) {
      Map<Pair<Character, Character>, AtomicLong> next = new HashMap<>();
      pairCounts.forEach((p, count) -> {
        Character insert = map.get(p);
        if (insert == null) {
          inc(next, p, count);
        } else {
          inc(next, Pair.of(p.a(), insert), count);
          inc(next, Pair.of(insert, p.b()), count);
        }
      });
      pairCounts = next;
    }

    Map<Character, AtomicLong> counts = new HashMap<>();
    pairCounts.forEach((p, count) -> {
      inc(counts, p.a(), count);
      inc(counts, p.b(), count);
    });
    counts.remove('0');
    Collection<AtomicLong> values = counts.values();
    long max = values.stream().mapToLong(AtomicLong::longValue).max().getAsLong();
    long min = values.stream().mapToLong(AtomicLong::longValue).min().getAsLong();
    return (max - min) / 2;
  }

  private static <T> void inc(Map<T, AtomicLong> map, T key, AtomicLong value) {
    map.computeIfAbsent(key, x -> new AtomicLong()).addAndGet(value.longValue());
  }


}
