package aoc2021;

import util.BitSet;
import util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day22 implements Day {


  private final List<Line> input;

  public Day22(List<String> input) {
    this.input = input.stream()
            .filter(s -> !s.isEmpty())
            .map(s -> s.split("[ =,.]+"))
            .map(Line::new)
            .collect(Collectors.toList());
  }

  public static Day22 fromResource(String name) {
    return new Day22(Util.readResource(name));
  }

  public long solvePart1() {
    return solve(input.stream().map(Line::bounded).filter(Objects::nonNull).collect(Collectors.toList()));
  }

  public long solvePart2() {
    return solve(input);
  }

  private long solve(List<Line> input) {
    TreeSet<Integer> xset = new TreeSet<>();
    TreeSet<Integer> yset = new TreeSet<>();
    TreeSet<Integer> zset = new TreeSet<>();

    for (Line line : input) {
      xset.add(line.x1);
      xset.add(line.x2);
      yset.add(line.y1);
      yset.add(line.y2);
      zset.add(line.z1);
      zset.add(line.z2);
    }

    int[] xrev = map(xset);
    int[] yrev = map(yset);
    int[] zrev = map(zset);
    Map<Integer, Integer> xmap = rev(xrev);
    Map<Integer, Integer> ymap = rev(yrev);
    Map<Integer, Integer> zmap = rev(zrev);

    int xsize = xrev.length;
    int ysize = yrev.length;
    int zsize = zrev.length;

    //BitSet m = new BitSet(xsize * ysize * zsize);
    boolean[][][] m = new boolean[xsize][ysize][zsize];

    int xfactor = zsize * ysize;

    for (Line line : input) {
      boolean on = line.on;
      int x1 = xmap.get(line.x1);
      int x2 = xmap.get(line.x2);
      int y1 = ymap.get(line.y1);
      int y2 = ymap.get(line.y2);
      int z1 = zmap.get(line.z1);
      int z2 = zmap.get(line.z2);
      for (int x = x1; x < x2; x++) {
        //int xpart = x * xfactor;
        for (int y = y1; y < y2; y++) {
          //int ypart = xpart + y * zsize;
          for (int z = z1; z < z2; z++) {
            //m.set(ypart + z, on);
            m[x][y][z] = on;
          }
        }
      }
    }

    long count = 0;
    for (int x = 0; x < xsize; x++) {
      //int xpart = x * xfactor;
      for (int y = 0; y < ysize; y++) {
        //int ypart = xpart + y * zsize;
        for (int z = 0; z < zsize; z++) {
          if (m[x][y][z]) {
          //if (m.get(ypart + z)) {
            long x1 = xrev[x];
            long x2 = xrev[x + 1];
            long y1 = yrev[y];
            long y2 = yrev[y + 1];
            long z1 = zrev[z];
            long z2 = zrev[z + 1];
            count += (x2 - x1) * (y2 - y1) * (z2 - z1);
          }
        }
      }
    }
    return count;
  }

  private Map<Integer, Integer> rev(int[] list) {
    HashMap<Integer, Integer> res = new HashMap<>(list.length);
    for (int i = 0; i < list.length; i++) {
      res.put(list[i], i);
    }
    return res;
  }

  private int[] map(Set<Integer> set) {
    int[] res = new int[set.size()];
    int index = 0;
    for (Integer integer : set) {
      res[index++] = integer;
    }
    return res;
  }

  static class Line {
    final boolean on;
    final int x1;
    final int x2;
    final int y1;
    final int y2;
    final int z1;
    final int z2;

    public Line(boolean on, int x1, int x2, int y1, int y2, int z1, int z2) {
      this.on = on;
      this.x1 = x1;
      this.x2 = x2;
      this.y1 = y1;
      this.y2 = y2;
      this.z1 = z1;
      this.z2 = z2;
    }

    Line(String[] parts) {
      on = parts[0].equals("on");
      x1 = Integer.parseInt(parts[2]);
      x2 = Integer.parseInt(parts[3]) + 1;
      y1 = Integer.parseInt(parts[5]);
      y2 = Integer.parseInt(parts[6]) + 1;
      z1 = Integer.parseInt(parts[8]);
      z2 = Integer.parseInt(parts[9]) + 1;
    }

    public Line bounded() {
      int x1 = Math.max(-50, this.x1);
      int x2 = Math.min(51, this.x2);
      int y1 = Math.max(-50, this.y1);
      int y2 = Math.min(51, this.y2);
      int z1 = Math.max(-50, this.z1);
      int z2 = Math.min(51, this.z2);
      if (x1 >= x2 || y1 >= y2 || z1 >= z2) {
        return null;
      }
      return new Line(on, x1, x2, y1, y2, z1, z2);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Line line = (Line) o;
      return on == line.on &&
              x1 == line.x1 &&
              x2 == line.x2 &&
              y1 == line.y1 &&
              y2 == line.y2 &&
              z1 == line.z1 &&
              z2 == line.z2;
    }

    @Override
    public int hashCode() {
      return Objects.hash(on, x1, x2, y1, y2, z1, z2);
    }

    @Override
    public String toString() {
      return "Line{" +
              "on=" + on +
              ", x1=" + x1 +
              ", x2=" + x2 +
              ", y1=" + y1 +
              ", y2=" + y2 +
              ", z1=" + z1 +
              ", z2=" + z2 +
              '}';
    }
  }
}