package aoc;

import intcode.IntCode;

import java.math.BigInteger;
import java.util.List;

public class Day19 {

  private final String name;

  public Day19(String name) {
    this.name = name;
  }

  public int part1() {
    int sum = 0;
    for (int y = 0; y < 50; y++) {
      for (int x = 0; x < 50; x++) {
        int value = hitBeam(x, y) ? 1 : 0;
        sum += value;
        System.out.print(value == 0 ? '.' : "#");
      }
      System.out.println();
    }

    return sum;
  }

  public int part2() {
    int x = 0;
    for (int y = 100; true; y++) {
      while (!hitBeam(x, y)) {
        x++;
      }
      if (hitBeam(x + 99, y - 99)) {
        return x * 10000 + y - 99;
      }
    }
  }

  private boolean hitBeam(int x, int y) {
    IntCode intcode = IntCode.fromResource(name);
    intcode.writeStdin(x);
    intcode.writeStdin(y);
    intcode.run();
    List<BigInteger> output = intcode.drainStdout();
    if (output.size() != 1) {
      throw new RuntimeException("Unexpected " + output);
    }
    return 1 == output.get(0).intValueExact();
  }

}
