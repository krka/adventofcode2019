package aoc2023;

import util.Day;
import util.Grid;
import util.Pair;
import util.Util;
import util.Vec2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day21 implements Day {
  private final List<List<String>> lines;
  private final Grid<Character> grid;
  private final Vec2 start;

  public Day21(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    grid = Grid.from(lines.get(0), '#');
    start = grid.stream().filter(characterEntry -> characterEntry.getValue() == 'S').findAny().get().getPos();
    grid.set(start, '.');
  }

  @Override
  public long solvePart1() {
    Set<Vec2> cur = new HashSet<>(List.of(start));
    for (int i = 0; i < 64; i++) {
      cur = cur.stream()
              .flatMap(point -> Vec2.DIRS.stream().map(point::add))
              .filter(point -> grid.get(point) == '.')
              .collect(Collectors.toSet());
    }
    return cur.size();
  }

  @Override
  public long solvePart2() {
    Set<Vec2> cur = new HashSet<>(List.of(start));
    final Vec2 offset = Vec2.grid(grid.rows(), grid.cols());

    long total = 26501365L;
    long delta = total % 131;

    List<Pair<Integer, Integer>> dataPoints = new ArrayList<>();
    for (int steps = 0; true; steps++) {
      if (delta == steps % 131) {
        dataPoints.add(Pair.of(steps, cur.size()));
        if (dataPoints.size() == 3) {
          break;
        }
      }
      cur = cur.stream()
              .flatMap(point -> Vec2.DIRS.stream().map(point::add))
              .filter(point -> grid.get(point.mod(offset)) == '.')
              .collect(Collectors.toSet());
    }

    dataPoints.forEach(pair -> System.out.printf("f(%d) = %d%n", pair.a(), pair.b()));

    return fun(total);
  }

  private long fun(long x) {
    // Values from wolfram-alpha, solving for:
    // f(65) = 3726
    // f(196) = 33086
    // f(327) = 91672
    final BigInteger bigX = BigInteger.valueOf(x);
    final BigInteger bigX2 = bigX.pow(2);
    BigInteger ba = bigX2.multiply(BigInteger.valueOf(14613));
    BigInteger bb = bigX.multiply(BigInteger.valueOf(32167));
    BigInteger bc = BigInteger.valueOf(111106);
    BigInteger bans = ba.add(bb).add(bc);
    final BigInteger[] bigIntegers = bans.divideAndRemainder(BigInteger.valueOf(17161L));
    if (bigIntegers[1].equals(BigInteger.ZERO)) {
      return bigIntegers[0].longValueExact();
    }
    throw new RuntimeException();
  }
}

