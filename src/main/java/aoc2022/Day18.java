package aoc2022;

import util.Day;
import util.Util;
import util.Vec3;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Day18 implements Day {
  private final Set<Vec3> set;
  private final int minX;
  private final int maxX;
  private final int minY;
  private final int maxY;
  private final int minZ;
  private final int maxZ;

  public Day18(String name) {
    set = Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .map(Vec3::parse)
            .collect(Collectors.toSet());
    minX = (int) set.stream().mapToLong(Vec3::getX).min().getAsLong();
    maxX = (int) set.stream().mapToLong(Vec3::getX).max().getAsLong();
    minY = (int) set.stream().mapToLong(Vec3::getY).min().getAsLong();
    maxY = (int) set.stream().mapToLong(Vec3::getY).max().getAsLong();
    minZ = (int) set.stream().mapToLong(Vec3::getZ).min().getAsLong();
    maxZ = (int) set.stream().mapToLong(Vec3::getZ).max().getAsLong();

  }


  @Override
  public long solvePart1() {
    int size = set.size() * 6;
    for (Vec3 v : set) {
      for (Vec3 dir : Vec3.DIR_6) {
        if (set.contains(v.add(dir))) {
          size--;
        }
      }
    }
    return size;
  }

  @Override
  public long solvePart2() {
    Set<Vec3> visited = new HashSet<>();
    Queue<Vec3> q = new LinkedBlockingQueue<>();

    final Vec3 start = new Vec3(minX - 1, minY - 1, minZ - 1);
    visited.add(start);
    q.add(start);

    int count = 0;
    while (!q.isEmpty()) {
      final Vec3 pos = q.poll();
      if (inbound((int) pos.getX(), (int) pos.getY(), (int) pos.getZ())) {
        for (Vec3 dir : Vec3.DIR_6) {
          final Vec3 nextPos = pos.add(dir);
          if (set.contains(nextPos)) {
            count++;
          } else {
            if (visited.add(nextPos)) {
              q.add(nextPos);
            }
          }
        }
      }
    }
    return count;
  }

  private boolean inbound(int x, int y, int z) {
    if (x < minX - 1) {
      return false;
    }
    if (y < minY - 1) {
      return false;
    }
    if (z < minZ - 1) {
      return false;
    }
    if (x > maxX + 1) {
      return false;
    }
    if (y > maxY + 1) {
      return false;
    }
    if (z > maxZ + 1) {
      return false;
    }
    return true;
  }
}



