package aoc2023;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day15 implements Day {
  private final List<List<String>> lines;

  public Day15(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
  }

  @Override
  public long solvePart1() {
    final String s = lines.get(0).get(0);
    final String[] parts = s.split(",");
    long sum = 0;
    for (String part : parts) {
      sum += hash213(part);
    }
    return sum;
  }

  private long hash213(String part) {
    long sum = 0;
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
    Map<Integer, List<Foo>> boxes = new HashMap<>();
    for (String part : parts) {
      final String[] parts2 = part.split("[=-]");
      final String label = parts2[0];
      final int box = (int) hash213((label));
      if (part.contains("=")) {
        final List<Foo> strings = boxes.computeIfAbsent(box, x -> new ArrayList<>());
        final int ocal = Integer.parseInt(parts2[1]);
        final Foo e = new Foo(label, ocal);
        final int i = strings.indexOf(e);
        if (i != -1) {
          strings.get(i).focal = ocal;
        } else {
          strings.add(e);
        }
      } else if (part.contains("-")) {
        final List<Foo> strings = boxes.computeIfAbsent(box, x -> new ArrayList<>());
        strings.remove(new Foo(label, -1));
      } else {
        throw new RuntimeException();
      }
    }
    long sum = 0;
    for (Map.Entry<Integer, List<Foo>> e : boxes.entrySet()) {
      final int a = e.getKey() + 1;
      final List<Foo> list = e.getValue();
      for (int j = 0; j < list.size(); j++) {
        final int c = list.get(j).focal;
        sum += (long) a * (j + 1) * c;
      }
    }
    return sum;
  }

  static class Foo {
    String name;
    int focal;

    public Foo(String name, int focal) {
      this.name = name;
      this.focal = focal;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Foo foo = (Foo) o;
      return Objects.equals(name, foo.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }
  }
}

