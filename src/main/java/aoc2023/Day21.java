package aoc2023;

import util.Day;
import util.Grid;
import util.Polynomial;
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

    List<Vec2> dataPoints = new ArrayList<>();
    for (int steps = 0; true; steps++) {
      if (delta == steps % 131) {
        dataPoints.add(Vec2.of(steps, cur.size()));
        if (dataPoints.size() == 3) {
          break;
        }
      }
      cur = cur.stream()
              .flatMap(point -> Vec2.DIRS.stream().map(point::add))
              .filter(point -> grid.get(point.mod(offset)) == '.')
              .collect(Collectors.toSet());
    }

    final Polynomial poly = Polynomial.solve(dataPoints);
    return poly.evaluate(total).toLongExact();
  }
}

