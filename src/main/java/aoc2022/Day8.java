package aoc2022;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.function.Function;

public class Day8 implements Day {

  private final Grid<Character> grid;

  public Day8(String name) {
    grid = Grid.from(Util.readResource(name), 'A', Function.identity());
  }

  @Override
  public long solvePart1() {
    final Grid<Boolean> count = new Grid<>(grid.rows(), grid.cols(), false);
    count2(count, 0, 0, 1, 0);
    count2(count, 0, 0, 0, 1);
    count2(count, grid.rows() - 1, grid.cols() - 1, -1, 0);
    count2(count, grid.rows() - 1, grid.cols() - 1, 0, -1);

    return count.count(x -> x);
  }

  private void count2(Grid<Boolean> count, int row, int col, int dx, int dy) {
    while (grid.inbound(row, col)) {
      count3(count, row, col, dy, dx);
      row += dx;
      col += dy;
    }
  }

  private void count3(Grid<Boolean> count, int row, int col, int dx, int dy) {
    int height = -1;
    while (grid.inbound(row, col)) {
      final int h = grid.get(row, col) - '0';
      if (h > height) {
        count.set(row, col, true);
      }
      height = Math.max(height, h);
      row += dx;
      col += dy;
    }
  }

  @Override
  public long solvePart2() {
    return grid.stream()
            .mapToLong(e -> scenic(e.getRow(), e.getCol(), e.getValue()))
            .max().getAsLong();
  }

  private long scenic(int row, int col, char height) {
    return Vec2.DIRS.stream().mapToLong(dir -> vis(dir, row, col, height)).reduce(1, (a, b) -> a * b);
  }

  private long vis(Vec2 dir, int row, int col, char height) {
    final Vec2 start = Vec2.of(col, row);
    int distance = 0;
    int count = 0;
    while (true) {
      final Vec2 pos = start.add(dir.multiply(++distance));
      if (!grid.inbound(pos)) {
        return count;
      }
      count++;
      if (height <= grid.get(pos)) {
        return count;
      }
    }
  }
}
