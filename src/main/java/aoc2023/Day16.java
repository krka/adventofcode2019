package aoc2023;

import util.BFS;
import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Day16 implements Day {
  private final List<List<String>> lines;
  private final Grid<Character> grid;

  public Day16(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    grid = Grid.from(lines.get(0), '.');
  }

  @Override
  public long solvePart1() {
    return bfs(Vec2.grid(0, 0), Vec2.EAST);
  }

  @Override
  public long solvePart2() {
    long best = 0;
    for (int r = 0; r < grid.rows(); r++) {
      best = Math.max(best, bfs(Vec2.grid(r, 0), Vec2.EAST));
      best = Math.max(best, bfs(Vec2.grid(r, grid.cols() - 1), Vec2.WEST));
    }
    for (int c = 0; c < grid.cols(); c++) {
      best = Math.max(best, bfs(Vec2.grid(0, c), Vec2.SOUTH));
      best = Math.max(best, bfs(Vec2.grid(grid.rows() - 1, c), Vec2.NORTH));
    }
    return best;
  }

  private long bfs(Vec2 startPos, Vec2 startDir) {
    final BFS<Node> bfs = BFS.newBFS(Node.class)
            .withStart(new Node(startPos, startDir))
            .withEdgeFunction(x -> {
              final Vec2 pos = x.pos;
              if (grid.inbound(x.pos)) {
                final Vec2 dir = x.dir;
                final boolean vertical = dir.equals(Vec2.SOUTH) || dir.equals(Vec2.NORTH);
                int rotation = vertical ? 1 : -1;
                switch (grid.get(pos)) {
                  case '.': return Stream.of(nextPos(pos, dir));
                  case '\\': return Stream.of(nextPos(pos, dir.rotateLeft(rotation)));
                  case '/': return Stream.of(nextPos(pos, dir.rotateRight(rotation)));
                  case '|': {
                    if (vertical) {
                      return Stream.of(nextPos(pos, dir));
                    }
                    return Stream.of(nextPos(pos, Vec2.SOUTH), nextPos(pos, Vec2.NORTH));
                  }
                  case '-': {
                    if (vertical) {
                      return Stream.of(nextPos(pos, Vec2.EAST), nextPos(pos, Vec2.WEST));
                    }
                    return Stream.of(nextPos(pos, dir));
                  }
                  default:
                    throw new RuntimeException();
                }
              } else {
                return Stream.of();
              }
            }).build();
    bfs.run();
    return bfs.visited().stream().map(node -> node.pos)
            .filter(grid::inbound)
            .distinct()
            .count();
  }

  static Node nextPos(Vec2 pos, Vec2 dir) {
    return new Node(pos.add(dir), dir);
  }

  static class Node {
    final Vec2 pos;
    final Vec2 dir;

    Node(Vec2 pos, Vec2 dir) {
      this.pos = pos;
      this.dir = dir;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Node x = (Node) o;
      return Objects.equals(pos, x.pos) && Objects.equals(dir, x.dir);
    }

    @Override
    public int hashCode() {
      return Objects.hash(pos, dir);
    }
  }
}

