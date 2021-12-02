package aoc2021;

import util.Util;

import java.util.List;

public class Day2 {

  private final List<String> input;

  public Day2(String name) {
    input = Util.readResource(name);
  }

  public long solvePart1() {
    int x = 0;
    int y = 0;
    for (String s : input) {
      if (s.isEmpty()) {
        break;
      }
      String[] parts = s.split(" ");
      String dir = parts[0];
      int magnitude = Integer.parseInt(parts[1]);
      switch (dir) {
        case "forward": x += magnitude; break;
        case "up": y -= magnitude; break;
        case "down": y += magnitude; break;
        default: throw new RuntimeException();
      }
    }
    return x * y;
  }

  public long solvePart2() {
    long x = 0;
    long y = 0;
    long aim = 0;
    for (String s : input) {
      if (s.isEmpty()) {
        break;
      }
      String[] parts = s.split(" ");
      String dir = parts[0];
      long magnitude = Integer.parseInt(parts[1]);
      switch (dir) {
        case "forward": {
          x += magnitude;
          y += aim * magnitude;
          break;
        }
        case "up": aim -= magnitude; break;
        case "down": aim += magnitude; break;
        default: throw new RuntimeException();
      }
    }
    return x * y;
  }

}
