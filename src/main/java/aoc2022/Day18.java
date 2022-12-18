package aoc2022;

import util.Day;
import util.Util;
import util.Vector3;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Day18 implements Day {
  private final Set<Vector3> set;
  private final int minX;
  private final int maxX;
  private final int minY;
  private final int maxY;
  private final int minZ;
  private final int maxZ;

  public Day18(String name) {
    set = Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .map(Vector3::parse)
            .collect(Collectors.toSet());
    minX = (int) set.stream().mapToLong(Vector3::getX).min().getAsLong();
    maxX = (int) set.stream().mapToLong(Vector3::getX).max().getAsLong();
    minY = (int) set.stream().mapToLong(Vector3::getY).min().getAsLong();
    maxY = (int) set.stream().mapToLong(Vector3::getY).max().getAsLong();
    minZ = (int) set.stream().mapToLong(Vector3::getZ).min().getAsLong();
    maxZ = (int) set.stream().mapToLong(Vector3::getZ).max().getAsLong();

  }


  @Override
  public long solvePart1() {
    int size = set.size() * 6;
    for (Vector3 v : set) {
      for (Vector3 dir : Vector3.DIR_6) {
        if (set.contains(v.add(dir))) {
          size--;
        }
      }
    }
    return size;
  }

  @Override
  public long solvePart2() {
    Set<Vector3> visited = new HashSet<>();
    Queue<Vector3> q = new LinkedBlockingQueue<>();

    final Vector3 start = new Vector3(minX - 1, minY - 1, minZ - 1);
    visited.add(start);
    q.add(start);

    int count = 0;
    while (!q.isEmpty()) {
      final Vector3 pos = q.poll();
      if (inbound((int) pos.getX(), (int) pos.getY(), (int) pos.getZ())) {
        for (Vector3 dir : Vector3.DIR_6) {
          final Vector3 nextPos = pos.add(dir);
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



