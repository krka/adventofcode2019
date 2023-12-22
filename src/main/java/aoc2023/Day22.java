package aoc2023;

import util.Day;
import util.Grid;
import util.Pair;
import util.Splitter;
import util.Util;
import util.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day22 implements Day {
  private final List<List<String>> lines;
  private final Map<Integer, Set<Integer>> supportedBy;
  private final Map<Integer, Set<Integer>> supporting;
  private final List<Pair<Vec3, Vec3>> bricks;
  private final Map<Vec3, Integer> world;
  private final HashSet<Integer> onGround;

  public Day22(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));

    world = new HashMap<>();
    bricks = new ArrayList<>();
    for (String string : lines.get(0)) {
      final List<String> split = Splitter.withoutDelim("~").split(string);
      final Vec3 p1 = Vec3.parse(split.get(0));
      final Vec3 p2 = Vec3.parse(split.get(1));

      Vec3 size = p2.sub(p1).add(Vec3.of(1, 1, 1));
      final Pair<Vec3, Vec3> brick = Pair.of(p1, size);
      bricks.add(brick);
    }

    bricks.sort(Comparator.comparingLong(vec3Vec3Pair -> vec3Vec3Pair.a().getZ()));
    for (int i = 0; i < bricks.size(); i++) {
      addBrick(world, bricks.get(i), i);
    }

    for (int i = 0; i < bricks.size(); i++) {
      final Pair<Vec3, Vec3> brick = bricks.get(i);
      final Vec3 size = brick.b();
      long bx = brick.a().getX();
      long by = brick.a().getY();
      long bz = brick.a().getZ();
      while (bz > 1 && free(world, bx, by, bz - 1, size.getX(), size.getY())) {
        bz--;
      }
      final Pair<Vec3, Vec3> newBrick = Pair.of(Vec3.of(bx, by, bz), size);
      removeBrick(world, brick, i);
      addBrick(world, newBrick, i);
      bricks.set(i, newBrick);
    }

    supportedBy = new HashMap<>();
    supporting = new HashMap<>();
    for (int i = 0; i < bricks.size(); i++) {
      supportedBy.put(i, new HashSet<>());
      supporting.put(i, new HashSet<>());
    }
    for (int i = 0; i < bricks.size(); i++) {
      final Pair<Vec3, Vec3> brick = bricks.get(i);
      final Vec3 p1 = brick.a();
      final Vec3 size = brick.b();
      long z = size.getZ();
      final Set<Integer> supporting2 = supporting(world, p1, z, size.getX(), size.getY());
      int finalI = i;
      supporting2.forEach(integer -> supportedBy.get(integer).add(finalI));
      supporting.get(i).addAll(supporting2);
    }
    onGround = new HashSet<>();
    for (int i = 0; i < bricks.size(); i++) {
      if (bricks.get(i).a().getZ() == 1) {
        supportedBy.get(i).add(-1); // ground
        onGround.add(i);
      }
    }
  }

  @Override
  public long solvePart1() {
    for (int i = 0; i < bricks.size(); i++) {
      final Pair<Vec3, Vec3> brick = bricks.get(i);
      final Vec3 p1 = brick.a();
      final Vec3 size = brick.b();
      long z = size.getZ();
      final Set<Integer> supporting2 = supporting(world, p1, z, size.getX(), size.getY());
      int finalI = i;
      supporting2.forEach(integer -> supportedBy.get(integer).add(finalI));
      supporting.get(i).addAll(supporting2);
    }
    return supporting.values().stream()
            .mapToInt(bricks -> bricks.stream()
                    .noneMatch(brick -> supportedBy.get(brick).size() == 1) ? 1 : 0)
            .sum();
  }

  @Override
  public long solvePart2() {
    int sum = 0;
    for (int i = 0; i < bricks.size(); i++) {
      final Set<Integer> visited = new HashSet<>();
      visited.add(i);
      onGround.forEach(brickIndex -> reachable(brickIndex, visited));
      sum += bricks.size() - visited.size();
    }
    return sum;
  }

  private void reachable(int brickIndex, Set<Integer> visited) {
    if (visited.add(brickIndex)) {
      supporting.get(brickIndex).forEach(integer -> reachable(integer, visited));
    }
  }

  private Set<Integer> supporting(Map<Vec3, Integer> world, Vec3 p1, long sz, long sx, long sy) {
    Set<Integer> supporting = new HashSet<>();
    for (int x = 0; x < sx; x++) {
      for (int y = 0; y < sy; y++) {
        final Vec3 point = p1.add(Vec3.of(x, y, sz));
        if (world.containsKey(point)) {
          supporting.add(world.get(point));
        }
      }
    }
    return supporting;
  }

  private void removeBrick(Map<Vec3, Integer> world, Pair<Vec3, Vec3> brick, int i) {
    final Vec3 p1 = brick.a();
    final Vec3 size = brick.b();
    for (int x = 0; x < size.getX(); x++) {
      for (int y = 0; y < size.getY(); y++) {
        for (int z = 0; z < size.getZ(); z++) {
          if (i != world.remove(p1.add(Vec3.of(x, y, z)))) {
            throw new RuntimeException();
          }
        }
      }
    }
  }

  private void addBrick(Map<Vec3, Integer> world, Pair<Vec3, Vec3> brick, int i) {
    final Vec3 p1 = brick.a();
    final Vec3 size = brick.b();
    for (int x = 0; x < size.getX(); x++) {
      for (int y = 0; y < size.getY(); y++) {
        for (int z = 0; z < size.getZ(); z++) {
          if (null != world.put(p1.add(Vec3.of(x, y, z)), i)) {
            throw new RuntimeException();
          }
        }
      }
    }

  }

  private boolean free(Map<Vec3, Integer> world, long bx, long by, long bz, long sx, long sy) {
    for (int x = 0; x < sx; x++) {
      for (int y = 0; y < sy; y++) {
        if (world.containsKey(Vec3.of(bx + x, by + y, bz))) {
          return false;
        }
      }
    }
    return true;
  }
}

