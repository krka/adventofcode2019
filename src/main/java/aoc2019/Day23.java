package aoc2019;

import intcode.IntCode;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
    Queue<IntCode> toRun = new LinkedList<>();

    for (int i = 0; i < 50; i++) {
      computers[i] = IntCode.fromResource(name);
      computers[i].writeStdin(i);
      computers[i].writeStdin(-1);
      toRun.add(computers[i]);
    }

    BigInteger natX = null;
    BigInteger natY = null;
    BigInteger prevNatY = null;

    while (true) {
      while (!toRun.isEmpty()) {
        IntCode computer = toRun.poll();
        computer.run();
        List<BigInteger> out = computer.drainStdout();
        if (0 != out.size() % 3) {
          throw new RuntimeException();
        }
        Iterator<BigInteger> iterator = out.iterator();
        while (iterator.hasNext()) {
          int d = iterator.next().intValueExact();
          BigInteger x = iterator.next();
          BigInteger y = iterator.next();
          if (d == 255) {
            natX = x;
            natY = y;
          } else {
            sendPacket(toRun, computers[d], x, y);
          }
        }
      }
      if (natX == null) {
        throw new RuntimeException();
      }
      if (natY.equals(prevNatY)) {
        return natY.intValueExact();
      }
      sendPacket(toRun, computers[0], natX, natY);
      prevNatY = natY;
    }
  }

  private void sendPacket(Queue<IntCode> toRun, IntCode destination, BigInteger x, BigInteger y) {
    destination.writeStdin(x);
    destination.writeStdin(y);
    toRun.add(destination);
  }
}
