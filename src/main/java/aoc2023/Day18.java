package aoc2023;

import util.Day;
import util.Pair;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.List;

public class Day18 implements Day {
  private final List<List<String>> lines;

  public Day18(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
  }

  @Override
  public long solvePart1() {
    Vec2 pos = Vec2.zero();
    List<Pair<Vec2, Vec2>> path = new ArrayList<>();
    for (String s : lines.get(0)) {
      final String[] parts = s.split(" ");
      String dir = parts[0];
      Vec2 vdir;
      switch (dir) {
        case "U": vdir = Vec2.NORTH; break;
        case "D": vdir = Vec2.SOUTH; break;
        case "L": vdir = Vec2.WEST; break;
        case "R": vdir = Vec2.EAST; break;
        default: throw new RuntimeException();
      }
      int distance = Integer.parseInt(parts[1]);

      pos = pos.add(vdir.multiply(distance));
      path.add(Pair.of(pos, vdir));
    }

    return areaOfGridEdge(path);

  }

  @Override
  public long solvePart2() {
    Vec2 pos = Vec2.zero();
    List<Pair<Vec2, Vec2>> path = new ArrayList<>();
    for (String s : lines.get(0)) {
      final String[] parts = s.split("[ #()]+");
      final String hex = parts[2];
      final char lastChar = hex.charAt(hex.length() - 1);
      final int distance = Integer.parseInt(hex.substring(0, 5), 16);
      Vec2 vdir;
      switch (lastChar) {
        case '3': vdir = Vec2.NORTH; break;
        case '1': vdir = Vec2.SOUTH; break;
        case '2': vdir = Vec2.WEST; break;
        case '0': vdir = Vec2.EAST; break;
        default: throw new RuntimeException();
      }

      pos = pos.add(vdir.multiply(distance));
      path.add(Pair.of(pos, vdir));
    }

    return areaOfGridEdge(path);
  }

  private static long areaOfGridEdge(List<Pair<Vec2, Vec2>> path) {
    final var path2 = new ArrayList<>(path);
    path2.add(path2.remove(0));
    final var zipped = Util.zip(path, path2);

    long sum = 0;
    long x1 = 0;
    long y1 = 0;
    long modx = 0;
    long mody = 0;

    for (Pair<Pair<Vec2, Vec2>, Pair<Vec2, Vec2>> pair : zipped) {
      final Vec2 pos = pair.a().a();
      final Vec2 dir = pair.a().b();
      final Vec2 nextDir = pair.b().b();

      if (dir.vertical()) {
        mody = (1 - nextDir.getX()) / 2;
      } else {
        modx = (1 + nextDir.getY()) / 2;
      }
      final long x2 = pos.getX() + modx;
      final long y2 = pos.getY() + mody;
      sum += x1 * y2 - x2 * y1;

      x1 = x2;
      y1 = y2;
    }

    return sum / 2;
  }
}

