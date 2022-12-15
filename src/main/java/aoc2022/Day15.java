package aoc2022;

import util.Day;
import util.Interval;
import util.IntervalSet;
import util.Pair;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day15 implements Day {
  private final List<String> input;
  private final List<Pair<Vec2, Vec2>> input2;

  private final int targetY;

  public Day15(String name, int targetY) {
    input = Util.readResource(name);
    this.targetY = targetY;

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
    List<Interval> intervals = new ArrayList<>();
    for (Pair<Vec2, Vec2> pair : input2) {
      final Vec2 sensor = pair.a();
      final Vec2 beacon = pair.b();

      final long manhattan = sensor.sub(beacon).manhattan();
      final long offset = Math.abs(sensor.getY() - targetY);
      final long m2 = manhattan - offset;
      final int minX = (int) (sensor.getX() - m2);
      final int maxX = (int) (sensor.getX() + m2) + 1;
      final Interval interval = Interval.of(minX, maxX);
      if (interval.contains(beacon.getX()) && beacon.getY() == targetY) {
        intervals.add(Interval.of(minX, beacon.getX()));
        intervals.add(Interval.of(beacon.getX() + 1, maxX));
      } else {
        intervals.add(interval);
      }
    }
    return IntervalSet.merge(intervals).length();
  }

  @Override
  public long solvePart2() {
    final ArrayList<Sensor> sensors = new ArrayList<>();
    Set<Long> diagB = new HashSet<>();
    Set<Long> diagA = new HashSet<>();
    for (Pair<Vec2, Vec2> pair : input2) {
      final Vec2 sensor = pair.a();
      final Vec2 beacon = pair.b();
      final int manhattan = (int) sensor.sub(beacon).manhattan();
      sensors.add(new Sensor(sensor, manhattan));

      final long a = sensor.getX() - sensor.getY();
      diagA.add(a - manhattan);
      diagA.add(a + manhattan);

      final long b = sensor.getX() + sensor.getY();
      diagB.add(b - manhattan);
      diagB.add(b + manhattan);

    }

    final Set<Long> diagsA = diagA.stream().filter(d -> diagA.contains(d + 2)).map(d -> d + 1).collect(Collectors.toSet());
    final Set<Long> diagsB = diagB.stream().filter(d -> diagB.contains(d + 2)).map(d -> d + 1).collect(Collectors.toSet());

    for (Long a : diagsA) {
      for (Long b : diagsB) {
        long y = b - a;
        if (0 != (y % 2)) {
          continue;
        }
        y /= 2;
        long x = b - y;
        if (safe(Vec2.of(x, y), sensors)) {
          return x * 4000000 + y;
        }
      }
    }
    throw new RuntimeException();
  }

  private boolean safe(Vec2 pos, ArrayList<Sensor> sensors) {
    for (Sensor sensor : sensors) {
      if (sensor.pos.sub(pos).manhattan() <= sensor.manhattan) {
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
  }
}



