package aoc2021;

import util.Grid;
import util.Util;
import util.Vec2;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Day15 implements Day {

  private final Grid<Integer> grid;

  public Day15(String name) {
    List<String> input = Util.readResource(name).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
    grid = Grid.from(input, 100, c -> c - '0');
  }

  public long solvePart1() {
    return solveGrid(grid);
  }

  public long solvePart2() {
    Grid<Integer> bigGrid = new Grid<>(grid.rows() * 5, grid.cols() * 5, 0);
    bigGrid.setGrid(0, 0, grid, 0, 0, grid.rows(), grid.cols());
    for (int i = grid.rows(); i < bigGrid.rows(); i++) {
      for (int j = 0; j < grid.cols(); j++) {
        bigGrid.set(i, j, (bigGrid.get(i - grid.rows(), j) % 9) + 1);
      }
    }
    for (int i = 0; i < bigGrid.rows(); i++) {
      for (int j = grid.cols(); j < bigGrid.cols(); j++) {
        bigGrid.set(i, j, (bigGrid.get(i, j - grid.cols()) % 9) + 1);
      }
    }

    return solveGrid(bigGrid);
  }

  private long solveGrid(Grid<Integer> grid) {
    Set<Vec2> visited = new HashSet<>();
    Vec2 target = Vec2.of(grid.cols() - 1, grid.rows() - 1);
    Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getEstimate));
    queue.add(new Node(Vec2.zero(), 0, target));
    while (true) {
      Node cur = queue.poll();
      if (cur.pos.equals(target)) {
        return cur.risk;
      }
      if (visited.add(cur.pos)) {
        Vec2.DIRS.forEach(dir -> {
          Vec2 newPos = cur.pos.add(dir);
          if (grid.inbound(newPos)) {
            queue.add(new Node(newPos, cur.risk + grid.get(newPos), target));
          }
        });
      }
    }
  }
  static class Node {
    Vec2 pos;
    int risk;
    int estimate;

    public Node(Vec2 pos, int risk, Vec2 target) {
      this.pos = pos;
      this.risk = risk;
      this.estimate = (int) (risk + target.sub(pos).manhattan());
    }

    public int getEstimate() {
      return estimate;
    }
  }
}
