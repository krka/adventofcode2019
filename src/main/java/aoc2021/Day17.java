package aoc2021;

import util.Day;
import util.Util;

public class Day17 implements Day {

  private final int x1;
  private final int x2;
  private final int y1;
  private final int y2;

  public Day17(String input) {
    String[] split = input.split("[=., ]+");
    x1 = Integer.parseInt(split[3]);
    x2 = Integer.parseInt(split[4]);
    y1 = Integer.parseInt(split[6]);
    y2 = Integer.parseInt(split[7]);
  }

  public static Day17 fromResource(String name) {
    return new Day17(Util.readResource(name).get(0));
  }

  public long solvePart1() {
    int maxSteps = x2 + 1;
    boolean okX[] = new boolean[maxSteps + 1];
    for (int dx = 0; dx <= maxSteps; dx++) {
      for (int steps = 0; steps <= maxSteps; steps++) {
        int m = Math.min(dx, steps);
        int x = dx * m - m*(m-1)/2;
        if (x1 <= x && x <= x2) {
          okX[steps] = true;
        }
      }
    }

    int bestDY = 0;
    for (int steps = 0; steps < okX.length; steps++) {
      if (okX[steps]) {
        int drop = steps * (steps - 1) / 2;
        for (int dy = 0; dy < 100; dy++) {
          int y = dy * steps - drop;
          if (y1 <= y && y <= y2) {
            bestDY = Math.max(bestDY, dy);
          }
        }
      }
    }
    int bestY = -1;
    int y = 0;
    for (int steps = 1; y > bestY; steps++) {
      bestY = y;
      y = bestDY * steps - steps * (steps - 1) / 2;
    }
    return bestY;
  }

  public long solvePart2() {
    int maxSteps = x2 + 1;
    int count = 0;
    for (int dx = 0; dx <= maxSteps; dx++) {
      for (int dy = -maxSteps; dy <= maxSteps; dy++) {
        boolean ok = false;
        for (int steps = 0; steps <= maxSteps; steps++) {
          int m = Math.min(dx, steps);
          int x = dx * m - m * (m - 1) / 2;

          int drop = steps * (steps - 1) / 2;
          int y = dy * steps - drop;
          if (x1 <= x && x <= x2) {
            if (y1 <= y && y <= y2) {
              ok = true;
            }
          }
        }
        if (ok) {
          count++;
        }
      }
    }
    return count;
  }

}
