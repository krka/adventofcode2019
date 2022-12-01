package aoc2021;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

public class Day22 implements Day {


  private final List<Box> input;

  public Day22(List<String> input) {
    this.input = input.stream()
            .filter(s -> !s.isEmpty())
            .map(s -> s.split("[ =,.]+"))
            .map(Box::new)
            .collect(Collectors.toList());
  }

  public static Day22 fromResource(String name) {
    return new Day22(Util.readResource(name));
  }

  public long solvePart1() {
    return solve(input.stream()
            .map(Box::bounded)
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
  }

  public long solvePart2() {
    return solve(input);
  }

  private long solve(List<Box> input) {
    return phase(input, Box::getX1, Box::getX2, Box::splitX,
            xboxes -> phase(xboxes, Box::getY1, Box::getY2, Box::splitY,
                    yboxes -> phase(yboxes, Box::getZ1, Box::getZ2, Box::splitZ,
                            this::sumVolumes)));
  }

  private long phase(List<Box> input,
                     ToIntFunction<Box> first,
                     ToIntFunction<Box> second,
                     SplitFunction splitter,
                     ToLongFunction<List<Box>> nextFun) {
    TreeMap<Integer, List<Box>> map = new TreeMap<>();
    for (Box box : input) {
      map.computeIfAbsent(first.applyAsInt(box), k -> new ArrayList<>());
      map.computeIfAbsent(second.applyAsInt(box), k -> new ArrayList<>());
    }

    for (Box box : input) {
      final int min = first.applyAsInt(box);
      final int max = second.applyAsInt(box) + 1;
      final SortedMap<Integer, List<Box>> subMap = map.subMap(min, max);
      int prev = first.applyAsInt(box);
      for (Map.Entry<Integer, List<Box>> entry : subMap.entrySet()) {
        int cur = entry.getKey();
        if (cur != prev) {
          entry.getValue().add(splitter.apply(box, prev, cur));
        }
        prev = cur;
      }
    }
    return map.values().stream().mapToLong(nextFun).sum();
  }

  private long sumVolumes(List<Box> values) {
    if (values.isEmpty()) {
      return 0;
    }
    final Box last = values.get(values.size() - 1);
    if (last.isOn()) {
      return last.volume();
    } else {
      return 0;
    }
  }

  static class Box {
    final boolean on;
    final int x1;
    final int x2;
    final int y1;
    final int y2;
    final int z1;
    final int z2;

    public Box(boolean on, int x1, int x2, int y1, int y2, int z1, int z2) {
      this.on = on;
      this.x1 = x1;
      this.x2 = x2;
      this.y1 = y1;
      this.y2 = y2;
      this.z1 = z1;
      this.z2 = z2;
    }

    Box(String[] parts) {
      on = parts[0].equals("on");
      x1 = Integer.parseInt(parts[2]);
      x2 = Integer.parseInt(parts[3]) + 1;
      y1 = Integer.parseInt(parts[5]);
      y2 = Integer.parseInt(parts[6]) + 1;
      z1 = Integer.parseInt(parts[8]);
      z2 = Integer.parseInt(parts[9]) + 1;
    }

    public Box bounded() {
      int x1 = Math.max(-50, this.x1);
      int x2 = Math.min(51, this.x2);
      int y1 = Math.max(-50, this.y1);
      int y2 = Math.min(51, this.y2);
      int z1 = Math.max(-50, this.z1);
      int z2 = Math.min(51, this.z2);
      if (x1 >= x2 || y1 >= y2 || z1 >= z2) {
        return null;
      }
      return new Box(on, x1, x2, y1, y2, z1, z2);
    }

    public boolean isOn() {
      return on;
    }

    public int getX1() {
      return x1;
    }

    public int getX2() {
      return x2;
    }

    public int getY1() {
      return y1;
    }

    public int getY2() {
      return y2;
    }

    public int getZ1() {
      return z1;
    }

    public int getZ2() {
      return z2;
    }

    public Box splitX(int x1, int x2) {
      return new Box(on, x1, x2, y1, y2, z1, z2);
    }

    public Box splitY(int y1, int y2) {
      return new Box(on, x1, x2, y1, y2, z1, z2);
    }

    public Box splitZ(int z1, int z2) {
      return new Box(on, x1, x2, y1, y2, z1, z2);
    }

    public long volume() {
      final long x = x2 - x1;
      final long y = y2 - y1;
      final long z = z2 - z1;
      return x * y * z;
    }
  }

  private interface SplitFunction {
    Box apply(Box box, int min, int max);
  }
}