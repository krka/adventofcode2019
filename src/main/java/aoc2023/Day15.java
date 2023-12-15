package aoc2023;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day15 implements Day {
  private final List<List<String>> lines;

  public Day15(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
  }

  @Override
  public long solvePart1() {
    final String s = lines.get(0).get(0);
    final String[] parts = s.split(",");
    return Stream.of(parts).mapToInt(this::hash).sum();
  }

  private int hash(String part) {
    int sum = 0;
    for (Character c : Util.toList(part)) {
      sum += c;
      sum *= 17;
      sum &= 255;
    }
    return sum;
  }

  @Override
  public long solvePart2() {
    final String line = lines.get(0).get(0);
    final String[] parts = line.split(",");
    Map<Integer, Map<String, AtomicInteger>> boxes = new HashMap<>();
    for (String s : parts) {
      final String[] split = s.split("[=-]");
      final String label = split[0];
      final int box = hash(label);
      final Map<String, AtomicInteger> list = boxes.computeIfAbsent(box, x -> new LinkedHashMap<>());
      if (s.contains("=")) {
        list.computeIfAbsent(label, x -> new AtomicInteger()).set(Integer.parseInt(split[1]));
      } else if (s.contains("-")) {
        list.remove(label);
      } else {
        throw new RuntimeException();
      }
    }
    long sum = 0;
    for (Map.Entry<Integer, Map<String, AtomicInteger>> e : boxes.entrySet()) {
      final int a = e.getKey() + 1;
      final Map<String, AtomicInteger> list = e.getValue();
      int k = 0;
      for (AtomicInteger focal : list.values()) {
        k++;
        sum += a * k * focal.get();
      }
    }
    return sum;
  }
}

