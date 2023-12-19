package aoc2023;

import util.Day;
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
    List<Vec2> path = new ArrayList<>();
    long totalDistance = 0;
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

      totalDistance += distance;
      pos = pos.add(vdir.multiply(distance));
      path.add(pos);
    }

    return Util.areaOfPoly(path) + 1 + totalDistance / 2;

  }

  @Override
  public long solvePart2() {
    Vec2 pos = Vec2.zero();
    List<Vec2> path = new ArrayList<>();
    long totalDistance = 0;
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

      totalDistance += distance;
      pos = pos.add(vdir.multiply(distance));
      path.add(pos);
    }

    return Util.areaOfPoly(path) + 1 + totalDistance / 2;
  }

}

