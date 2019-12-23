package aoc;

import intcode.IntCode;
import util.Util;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

public class Day23 {

  private final String name;

  public Day23(String name) {
    this.name = name;
  }

  public int part1() {
    IntCode[] computers = new IntCode[50];

    for (int i = 0; i < 50; i++) {
      computers[i] = IntCode.fromResource(name);
      computers[i].writeStdin(i);
      computers[i].writeStdin(-1);
    }

    while (true) {
      for (int i = 0; i < 50; i++) {
        computers[i].run();
        List<BigInteger> out = computers[i].drainStdout();
        if (0 != out.size() % 3) {
          throw new RuntimeException();
        }
        Iterator<BigInteger> iterator = out.iterator();
        while (iterator.hasNext()) {
          int d = iterator.next().intValueExact();
          BigInteger x = iterator.next();
          BigInteger y = iterator.next();
          if (d == 255) {
            return y.intValueExact();
          }
          computers[d].writeStdin(x);
          computers[d].writeStdin(y);
        }
      }
    }
  }

  public int part2() {
    IntCode[] computers = new IntCode[50];

    for (int i = 0; i < 50; i++) {
      computers[i] = IntCode.fromResource(name);
      computers[i].writeStdin(i);
      computers[i].writeStdin(-1);
    }

    BigInteger natX = null;
    BigInteger natY = null;
    BigInteger prevNatY = null;

    while (true) {
      boolean idle = true;
      for (int i = 0; i < 50; i++) {
        computers[i].run();
        List<BigInteger> out = computers[i].drainStdout();
        if (0 != out.size() % 3) {
          throw new RuntimeException();
        }
        Iterator<BigInteger> iterator = out.iterator();
        while (iterator.hasNext()) {
          idle = false;
          int d = iterator.next().intValueExact();
          BigInteger x = iterator.next();
          BigInteger y = iterator.next();
          if (d == 255) {
            natX = x;
            natY = y;
          } else {
            computers[d].writeStdin(x);
            computers[d].writeStdin(y);
          }
        }
      }
      if (idle) {
        if (natX == null) {
          throw new RuntimeException();
        }
        if (natY.equals(prevNatY)) {
          return natY.intValueExact();
        }
        computers[0].writeStdin(natX);
        computers[0].writeStdin(natY);
        prevNatY = natY;
      }
    }
  }
}
