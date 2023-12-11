package aoc2023;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Day10 implements Day {

  private final List<List<String>> lines;
  private final Grid<Character> grid;
  private final Vec2 startPos;
  private final List<Vec2> path;

  public Day10(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    grid = Grid.from(lines.get(0), '.', x -> x);
    startPos = grid.stream().filter(characterEntry -> characterEntry.getValue() == 'S').findAny().get().getPos();
    grid.set(startPos, startType());
    path = findPath();
  }

  private char startType() {
    String s = "";
    switch (grid.get(startPos.add(Vec2.NORTH))) {
      case '|':
      case 'F':
      case '7':
        s += "N";
    }
    switch (grid.get(startPos.add(Vec2.SOUTH))) {
      case '|':
      case 'L':
      case 'J':
        s += "S";
    }
    switch (grid.get(startPos.add(Vec2.WEST))) {
      case '-':
      case 'L':
      case 'F':
        s += "W";
    }
    switch (grid.get(startPos.add(Vec2.EAST))) {
      case '-':
      case '7':
      case 'J':
        s += "E";
    }

    switch (s) {
      case "NS": return '|';
      case "NW": return 'J';
      case "NE": return 'L';
      case "SW": return '7';
      case "SE": return 'F';
      case "WE": return '-';
      default: throw new RuntimeException("Unknown combo " + s);
    }
  }

  @Override
  public long solvePart1() {
    return (path.size() - 1) / 2;
  }

  @Override
  public long solvePart2() {
    final Set<Vec2> pathSet = new HashSet<>(path);

    int count = 0;
    for (int r = 0; r < grid.rows(); r++) {
      int inside = 0;
      for (int c = 0; c < grid.cols(); c++) {
        if (pathSet.contains(Vec2.grid(r, c))) {
          final char ch = grid.get(r, c);
          if (ch == '|' || ch == 'F' || ch == '7') {
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
    List<Vec2> path1 = new ArrayList<>();
    Vec2 pos = startPos;
    Vec2 prev = null;
    while (true) {
      path1.add(pos);
      char type = grid.get(pos);
      if (pos.equals(startPos) && path1.size() > 1) {
        break;
      }
      Vec2 finalPrev = prev;
      prev = pos;
      pos = nextPos(type, pos).filter(vec2 -> !vec2.equals(finalPrev)).findAny().get();
    }
    return path1;
  }

  private Stream<Vec2> nextPos(char type, Vec2 pos) {
    switch (type) {
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
