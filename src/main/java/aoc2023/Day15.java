package aoc2023;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    final String s = lines.get(0).get(0);
    final String[] parts = s.split(",");
    Map<Integer, List<Item>> boxes = new HashMap<>();
    for (String part : parts) {
      final String[] split = part.split("[=-]");
      final String label = split[0];
      final int box = hash((label));
      if (part.contains("=")) {
        final List<Item> items = boxes.computeIfAbsent(box, x -> new ArrayList<>());
        final int focal = Integer.parseInt(split[1]);
        final Item item = new Item(label, focal);
        final int i = items.indexOf(item);
        if (i != -1) {
          items.get(i).focal = focal;
        } else {
          items.add(item);
        }
      } else if (part.contains("-")) {
        boxes.computeIfAbsent(box, x -> new ArrayList<>()).remove(new Item(label, -1));
      } else {
        throw new RuntimeException();
      }
    }
    long sum = 0;
    for (Map.Entry<Integer, List<Item>> e : boxes.entrySet()) {
      final int a = e.getKey() + 1;
      final List<Item> list = e.getValue();
      for (int j = 0; j < list.size(); j++) {
        final int c = list.get(j).focal;
        sum += (long) a * (j + 1) * c;
      }
    }
    return sum;
  }

  static class Item {
    String name;
    int focal;

    public Item(String name, int focal) {
      this.name = name;
      this.focal = focal;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Item foo = (Item) o;
      return Objects.equals(name, foo.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }
  }
}

