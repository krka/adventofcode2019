package aoc2023;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class Day10 implements Day {

  private final List<List<String>> lines;
  private final Grid<Character> grid;

  public Day10(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    grid = Grid.from(lines.get(0), '.', x -> x);
  }

  @Override
  public long solvePart1() {
    return (findPath().size() - 1) / 2;
  }

  @Override
  public long solvePart2() {
    final List<Vec2> path = findPath();
    final Set<Vec2> pathSet = new HashSet<>(path);

    int count = 0;
    for (int r = 0; r < grid.rows(); r++) {
      int inside = 0;
      for (int c = 0; c < grid.cols(); c++) {
        if (pathSet.contains(Vec2.grid(r, c))) {
          final char ch = grid.get(r, c);
          if (ch == '|' || ch == 'F' || ch == '7' || ch == 'S') {
            inside = 1 - inside;
          }
        } else {
          count += inside;
        }
      }
    }
    return count;
  }

  private List<Vec2> findPath() {
    return findPath(grid.stream().filter(e -> e.getValue() == 'S').findAny().get().getPos());
  }

  private List<Vec2> findPath(Vec2 startPos) {
    List<Vec2> path = new ArrayList<>();
    Vec2 pos = startPos;
    Vec2 prev = null;
    while (true) {
      path.add(pos);
      char type = grid.get(pos);
      if (type == 'S' && path.size() > 1) {
        break;
      }
      Vec2 finalPrev = prev;
      prev = pos;
      pos = nextPos(type, pos).filter(vec2 -> !vec2.equals(finalPrev)).findAny().get();
    }
    return path;
  }

  private Stream<Vec2> nextPos(char type, Vec2 pos) {
    switch (type) {
      case 'S': // TODO: S == '|' works for my specific input, but likely wont work for all
      case '|':
        return Stream.of(pos.add(0, -1), pos.add(0, 1));
      case '-':
        return Stream.of(pos.add(-1, 0), pos.add(1, 0));
      case 'L':
        return Stream.of(pos.add(1, 0), pos.add(0, -1));
      case 'J':
        return Stream.of(pos.add(-1, 0), pos.add(0, -1));
      case '7':
        return Stream.of(pos.add(-1, 0), pos.add(0, 1));
      case 'F':
        return Stream.of(pos.add(1, 0), pos.add(0, 1));
      default:
        throw new RuntimeException();
    }
  }
}
