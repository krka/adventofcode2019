package aoc2021;

import util.Grid;
import util.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {

  private final List<String> input;
  private final Grid<Integer> grid;

  public Day9(String name) {
    this.input = Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    grid = Grid.from(input, 9, character -> (int) character - '0');
  }

  public long solvePart1() {
    int sum = 0;
    for (int i = 0; i < grid.rows(); i++) {
      for (int j = 0; j < grid.cols(); j++) {
        int cur = grid.get(i, j);
        if (cur < grid.get(i - 1, j) &&
                cur < grid.get(i + 1, j) &&
                cur < grid.get(i, j - 1) &&
                cur < grid.get(i, j + 1)) {
          sum += cur + 1;
        }
      }
    }
    return sum;
  }

  public long solvePart2() {
    List<Integer> basins = new ArrayList<>();
    for (int i = 0; i < grid.rows(); i++) {
      for (int j = 0; j < grid.cols(); j++) {
        int visit = visit(i, j);
        if (visit > 0) {
          basins.add(visit);
        }
      }
    }
    basins.sort(Comparator.comparingInt(Integer::intValue).reversed());
    long prod = 1;
    for (int i = 0; i < 3; i++) {
      prod *= basins.get(i);
    }
    return prod;
  }

  private int visit(int r, int c) {
    if (!grid.inbound(r, c)) {
      return 0;
    }
    int cur = grid.get(r, c);
    if (cur == 9) {
      return 0;
    }
    grid.set(r, c, 9);
    return 1 + visit(r - 1, c) +
            visit(r + 1, c) +
            visit(r, c - 1) +
            visit(r, c + 1)
            ;
  }

}
