package aoc2023;

import util.BFS;
import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Day17 implements Day {
  private final List<List<String>> lines;
  private final Grid<Integer> grid;

  public Day17(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    grid = Grid.from(lines.get(0), 0, x -> (int) x - '0');
  }

  @Override
  public long solvePart1() {
    return solve(1, 3);
  }

  @Override
  public long solvePart2() {
    return solve(4, 10);
  }

  private int solve(int min, int max) {
    final Vec2 target = Vec2.grid(grid.rows() - 1, grid.cols() - 1);
    final Node start1 = new Node(Vec2.grid(0, 0), Vec2.EAST, 0);
    final Node start2 = new Node(Vec2.grid(0, 0), Vec2.SOUTH, 0);
    return BFS.newBFS(start1, start2)
            .withTarget(vec2 -> vec2.pos.equals(target))
            .withCostFunction(Comparator.comparingInt(node -> node.heat))
            .withEdgeFunction(node -> {
              final List<Node> res = new ArrayList<>();
              addEdges(res, min, max, node.dir.rotateLeft(1), node.pos, node.heat);
              addEdges(res, min, max, node.dir.rotateRight(1), node.pos, node.heat);
              return res.stream();
            })
            .build().run()
            .getNode().heat;
  }

  private void addEdges(List<Node> res, int min, int max, Vec2 dir, Vec2 pos, int heat) {
    Vec2 next = pos;
    for (int i = 1; i <= max; i++) {
      next = next.add(dir);
      if (grid.inbound(next)) {
        heat += grid.get(next);
        if (i >= min) {
          res.add(new Node(next, dir, heat));
        }
      }
    }
  }

  static class Node {
    final Vec2 pos;
    final Vec2 dir;
    final int heat;

    public Node(Vec2 pos, Vec2 dir, int heat) {
      this.pos = pos;
      this.dir = dir;
      this.heat = heat;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Node node = (Node) o;
      return Objects.equals(pos, node.pos) && Objects.equals(dir, node.dir);
    }

    @Override
    public int hashCode() {
      return Objects.hash(pos, dir);
    }
  }
}

