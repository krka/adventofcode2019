package aoc2022;

import util.Day;
import util.Pair;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day15 implements Day {
  private final List<String> input;
  private final List<Pair<Vec2, Vec2>> input2;

  private final int targetY;
  private final int maxCoord;

  public Day15(String name, int targetY, int maxCoord) {
    input = Util.readResource(name);
    this.targetY = targetY;
    this.maxCoord = maxCoord;

    input2 = input.stream().filter(s -> !s.isEmpty())
            .map(s -> {
              final String[] parts = s.split("[=,:]");
              int sx = Integer.parseInt(parts[1]);
              int sy = Integer.parseInt(parts[3]);
              int bx = Integer.parseInt(parts[5]);
              int by = Integer.parseInt(parts[7]);

              Vec2 sensor = Vec2.of(sx, sy);
              Vec2 beacon = Vec2.of(bx, by);

              return Pair.of(sensor, beacon);
            }).collect(Collectors.toList());
  }

  @Override
  public long solvePart1() {
    Set<Integer> notBeacon = new HashSet<>();
    for (Pair<Vec2, Vec2> pair : input2) {
      final Vec2 sensor = pair.a();
      final Vec2 beacon = pair.b();

      final long manhattan = sensor.sub(beacon).manhattan();
      final long offset = Math.abs(sensor.getY() - targetY);
      final long m2 = manhattan - offset;
      final int minX = (int) (sensor.getX() - m2);
      final int maxX = (int) (sensor.getX() + m2);
      for (int i = minX; i <= maxX; i++) {
        if (i != beacon.getX() || beacon.getY() != targetY) {
          notBeacon.add(i);
        }
      }
    }

    return notBeacon.size();
  }

  @Override
  public long solvePart2() {
    final ArrayList<Sensor> sensors = new ArrayList<>();
    for (Pair<Vec2, Vec2> pair : input2) {
      final Vec2 sensor = pair.a();
      final Vec2 beacon = pair.b();
      final int manhattan = (int) sensor.sub(beacon).manhattan();
      sensors.add(new Sensor(sensor, manhattan));
    }

    Set<Vec2> candidates = new HashSet<>();
    for (int y = 0; y <= maxCoord; y++) {
      int y2 = y;
      final List<Interval> intervals = sensors.stream()
              .map(s -> s.toInterval(y2))
              .filter(Objects::nonNull)
              .collect(Collectors.toList());
      for (Interval interval : intervals) {
        test2(interval.minX - 1, y, intervals, candidates);
        test2(interval.maxX + 1, y, intervals, candidates);
      }
    }
    if (candidates.size() != 1) {
      throw new RuntimeException();
    }
    final Vec2 ans = candidates.iterator().next();
    return ans.getX() * 4000000 + ans.getY();
  }

  private void test2(int x, int y, List<Interval> intervals, Set<Vec2> candidates) {
    if (test(x, intervals)) {
      candidates.add(Vec2.of(x, y));
    }
  }

  private boolean test(int x, List<Interval> intervals) {
    if (x < 0 || x > maxCoord) {
      return false;
    }
    for (Interval interval : intervals) {
      if (interval.getMinX() <= x && x <= interval.getMaxX()) {
        return false;
      }
    }
    return true;
  }

  private static class Sensor {
    final Vec2 pos;
    final int manhattan;

    public Sensor(Vec2 pos, int manhattan) {
      this.pos = pos;
      this.manhattan = manhattan;
    }

    Interval toInterval(int y) {
      final int offset = (int) Math.abs(pos.getY() - y);
      int m2 = manhattan - offset;
      final int minX = (int) (pos.getX() - m2);
      final int maxX = (int) (pos.getX() + m2);
      if (maxX >= minX) {
        return new Interval(minX, maxX);
      }
      return null;
    }
  }

  private static class Interval {
    private final int minX;
    private final int maxX;

    public Interval(int minX, int maxX) {
      this.minX = minX;
      this.maxX = maxX;
    }

    public int getMinX() {
      return minX;
    }

    public int getMaxX() {
      return maxX;
    }
  }
}



