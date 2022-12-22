package aoc2022;

import util.Day;
import util.Grid;
import util.Pair;
import util.Util;
import util.Vec2;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day22 implements Day {

  private final List<List<String>> collect;
  private final List<String> gridData;
  private final String instr;
  private final Grid<Character> grid;
  private final Vec2 start;

  public Day22(String name) {
    collect = Util.readResource(name).stream().collect(Util.splitBy(s -> s.isEmpty()));
    gridData = collect.get(0);
    instr = collect.get(1).get(0);
    grid = Grid.from(gridData, ' ', Function.identity());
    start = findStart(grid);
  }


  private static Vec2 findStart(Grid<Character> grid) {
    for (int col = 0; col < grid.cols(); col++) {
      if (grid.get(0, col) != ' ') {
        return Vec2.of(col, 0);
      }
    }
    throw new RuntimeException();
  }

  @Override
  public long solvePart1() {
    final Pattern pattern = Pattern.compile("(\\d+|[LR])");
    final Matcher matcher = pattern.matcher(instr);
    Vec2 cur = start;
    Vec2 dir = Vec2.EAST;
    while (matcher.find()) {
      final String g = matcher.group(1);

      if (g.equals("L")) {
        dir = dir.rotateLeft(1);
      } else if (g.equals("R")){
        dir = dir.rotateRight(1);
      } else {
        final int steps = Integer.parseInt(g);
        for (int i = 0; i < steps; i++) {
          Vec2 newPos = cur.add(dir);
          final char c = grid.get(newPos);
          if (c != '#') {
            if (c != ' ') {
              cur = newPos;
            } else {
              newPos = findEdge(grid, cur, dir);
              if (grid.get(newPos) != '#') {
                cur = newPos;
              }
            }
          }
        }
      }
    }

    final long row = cur.getY() + 1;
    final long col = cur.getX() + 1;
    return row * 1000 + col * 4 + dir2Val(dir);
  }

  private Vec2 findEdge(Grid<Character> grid, Vec2 cur, Vec2 dir) {
    while (grid.get(cur) != ' ') {
      cur = cur.sub(dir);
    }
    return cur.add(dir);
  }

  private int dir2Val(Vec2 dir) {
    if (dir.equals(Vec2.EAST)) {
      return 0;
    }
    if (dir.equals(Vec2.SOUTH)) {
      return 1;
    }
    if (dir.equals(Vec2.WEST)) {
      return 2;
    }
    if (dir.equals(Vec2.NORTH)) {
      return 3;
    }

    throw new RuntimeException();
  }

  @Override
  public long solvePart2() {
    final Pattern pattern = Pattern.compile("(\\d+|[LR])");
    final Matcher matcher = pattern.matcher(instr);
    Vec2 cur = start;
    Vec2 dir = Vec2.EAST;
    while (matcher.find()) {
      final String g = matcher.group(1);

      if (g.equals("L")) {
        dir = dir.rotateLeft(1);
      } else if (g.equals("R")){
        dir = dir.rotateRight(1);
      } else {
        final int steps = Integer.parseInt(g);
        for (int i = 0; i < steps; i++) {
          Vec2 newPos = cur.add(dir);
          final char c = grid.get(newPos);
          if (c != '#') {
            if (c != ' ') {
              cur = newPos;
            } else {
              final Pair<Vec2, Vec2> edge2 = findEdge2(grid, cur, dir);
              if (grid.get(edge2.a()) != '#') {
                cur = edge2.a();
                dir = edge2.b();
              }
            }
          }
        }
      }
    }

    final long row = cur.getY() + 1;
    final long col = cur.getX() + 1;
    return row * 1000 + col * 4 + dir2Val(dir);
  }

  private Pair<Vec2, Vec2> findEdge2(Grid<Character> grid, Vec2 cur, Vec2 dirv) {
    final long row = cur.getY();
    final long col = cur.getX();
    if (grid.cols() == 150) {
      if (dirv.equals(Vec2.NORTH)) {
        if (row == 0) {
          if (col >= 50 && col < 100) {
            // 1 north to 6 east
            return Pair.of(Vec2.of(0, col + 100), Vec2.EAST);
          } else if (col >= 100 && col < 150) {
            // 2 north to 6 north
            return Pair.of(Vec2.of(col - 100, 199), Vec2.NORTH);
          }
        } else if (row == 100) {
          if (col >= 0 && col < 50) {
            // 4 north to 3 west
            return Pair.of(Vec2.of(50, col + 50), Vec2.EAST);
          }
        }
      } else if (dirv.equals(Vec2.SOUTH)) {
        if (row == 199) {
          if (col >= 0 && col < 50) {
            // 6 south to 2 south
            return Pair.of(Vec2.of(col + 100, 0), Vec2.SOUTH);
          }
        } else if (row == 149) {
          if (col >= 50 && col < 100) {
            // 5 south to 6 west
            return Pair.of(Vec2.of(49, col + 100), Vec2.WEST);
          }
        } else if (row == 49) {
          if (col >= 100 & col < 150) {
            // 2 south to 3 west
            return Pair.of(Vec2.of(99, col - 50), Vec2.WEST);
          }
        }
      } else if (dirv.equals(Vec2.WEST)) {
        if (col == 0) {
          if (row >= 100 && row < 150) {
            // 4 west to 1 east
            return Pair.of(Vec2.of(50, 149 - row), Vec2.EAST);
          } else if (row >= 150 && row < 200) {
            // 6 west to 1 south
            return Pair.of(Vec2.of(row - 100, 0), Vec2.SOUTH);
          }
        } else if (col == 50) {
          if (row >= 0 && row < 50) {
            // 1 west to 4 east
            return Pair.of(Vec2.of(0, 149 - row), Vec2.EAST);
          } else if (row >= 50 && row < 100) {
            // 3 west to 4 south
            return Pair.of(Vec2.of(row - 50, 100), Vec2.SOUTH);
          }
        }
      } else if (dirv.equals(Vec2.EAST)) {
        if (col == 49) {
          if (row >= 150 && row < 200) {
            // 6 east to 5 north
            return Pair.of(Vec2.of(row - 100, 149), Vec2.NORTH);
          }
        } else if (col == 99) {
          if (row >= 50 && row < 100) {
            // 3 east to 2 north
            return Pair.of(Vec2.of(row + 50, 49), Vec2.NORTH);
          } else if (row >= 100 && row < 150) {
            // 5 east to 2 west
            return Pair.of(Vec2.of(149, 149 - row), Vec2.WEST);
          }
        } else if (col == 149) {
          if (row >= 0 && row < 50) {
            // 2 east to 5 west
            return Pair.of(Vec2.of(99, 149 - row), Vec2.WEST);
          }
        }
      }
    }
    throw new RuntimeException();
  }
}

