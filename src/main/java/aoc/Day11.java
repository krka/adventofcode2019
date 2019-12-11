package aoc;

import intcode.IntCode;

import java.util.HashMap;
import java.util.Map;

public class Day11 {

  private final IntCode intCode;

  public Day11(String name) {
    intCode = IntCode.fromResource(name);
  }

  public Map<String, Integer> solve(boolean init) {
    int x = 0;
    int y = 0;
    int dir = 0;
    Map<String, Integer> map = new HashMap<>();

    // Initial color
    if (init) {
      map.put(x + "," + y, 1);
    }

    intCode.run();
    while (intCode.getState() == IntCode.State.WAITING_FOR_INPUT) {
      String coord = x + "," + y;
      int current = map.getOrDefault(coord, 0);
      intCode.writeStdin(current);
      intCode.run();
      int newColor = intCode.getStdout().poll().intValueExact();
      int rotation = intCode.getStdout().poll().intValueExact();
      map.put(coord, newColor);
      dir = (dir + 1 + (1 - rotation) * 2) % 4;
      switch (dir) {
        case 0: y--; break;
        case 1: x++; break;
        case 2: y++; break;
        case 3: x--; break;
        default: throw new RuntimeException("Unexpected direction: " + dir);
      }
    }

    return map;
  }
}
