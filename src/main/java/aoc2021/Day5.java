package aoc2021;

import util.Pair;
import util.Util;
import util.Vec2;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day5 {

  private final List<Pair<Vec2, Vec2>> input;

  public Day5(String name) {
    input = Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .map(Day5::toLine)
            .collect(Collectors.toList());
  }

  private static Pair<Vec2, Vec2> toLine(String s) {
    String[] split = s.split(" -> ");
    return Pair.of(Vec2.parse(split[0]), Vec2.parse(split[1]));
  }

  public long solvePart1() {
    return solve(delta -> isStraight(delta));
  }

  public long solvePart2() {
    return solve(delta -> isStraight(delta) || isDiagonal(delta));
  }

  private static boolean isStraight(Vec2 delta) {
    return delta.getX() == 0 || delta.getY() == 0;
  }

  private static boolean isDiagonal(Vec2 delta) {
    return Math.abs(delta.getX()) == Math.abs(delta.getY());
  }

  private long solve(Predicate<Vec2> filter) {
    int counts[][] = new int[1000][1000];
    int res = 0;
    for (Pair<Vec2, Vec2> pair : input) {
      Vec2 start = pair.a();
      Vec2 end = pair.b();
      Vec2 delta = end.sub(start);
      if (filter.test(delta)) {
        delta = delta.signum();
        for (Vec2 i = start; true; i = i.add(delta)) {
          if (++counts[(int) i.getX()][(int) i.getY()] == 2) res++;
          if (i.equals(end)) break;
        }
      }
    }
    return res;
  }

}
