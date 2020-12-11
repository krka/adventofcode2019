package aoc2020;

import org.junit.Test;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;

public class Day11Test {

  public static final String DAY = "11";

  @Test
  public void testPart1() {
    assertEquals(2344, solvePart1("2020/day" + DAY + ".in"));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(37, solvePart1("2020/day" + DAY + "-sample.in"));
  }

  @Test
  public void testPart2() {
    assertEquals(2076, solvePart2("2020/day" + DAY + ".in"));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(26, solvePart2("2020/day" + DAY + "-sample.in"));
  }

  enum State {
    OUTSIDE("X"), FLOOR("."), EMPTY("L"), OCCUPIED("#");

    private final String s;

    State(String s) {
      this.s = s;
    }

    @Override
    public String toString() {
      return s;
    }
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    Grid<State> grid = Grid.from(input, State.OUTSIDE, character -> character == 'L' ? State.EMPTY : State.FLOOR);

    AtomicBoolean run = new AtomicBoolean(true);
    while (run.get()) {
      run.set(false);
      Grid<State> copy = grid.duplicate();

      Grid<State> finalGrid = grid;
      grid.forEach((row, col, value) -> {
        int adj = adj(finalGrid, row, col);
        if (value == State.EMPTY && adj == 0) {
          copy.set(row, col, State.OCCUPIED);
          run.set(true);
        } else if (value == State.OCCUPIED && adj >= 4) {
          copy.set(row, col, State.EMPTY);
          run.set(true);
        }
      });

      grid = copy;
    }

    return grid.count(value -> value == State.OCCUPIED);
  }

  private int adj(Grid<State> grid, int row, int col) {
    int count = 0;
    for (Vec2 dir : Vec2.DIRS_8) {
      if (grid.get(row + dir.getY(), col + dir.getX()) == State.OCCUPIED) {
        count++;
      }
    }
    return count;
  }


  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);

    Grid<State> grid = Grid.from(input, State.OUTSIDE, character -> character == 'L' ? State.EMPTY : State.FLOOR);

    AtomicBoolean run = new AtomicBoolean(true);
    while (run.get()) {
      run.set(false);
      Grid<State> copy = grid.duplicate();

      Grid<State> finalGrid = grid;
      grid.forEach((row, col, value) -> {
        int adj = adj2(finalGrid, row, col);
        if (value == State.EMPTY && adj == 0) {
          copy.set(row, col, State.OCCUPIED);
          run.set(true);
        } else if (value == State.OCCUPIED && adj >= 5) {
          copy.set(row, col, State.EMPTY);
          run.set(true);
        }
      });

      grid = copy;
    }

    return grid.count(value -> value == State.OCCUPIED);
  }

  private int adj2(Grid<State> grid, int row, int col) {
    int count = 0;
    for (Vec2 dir : Vec2.DIRS_8) {
      State val = scan(grid, row, col, dir);
      if (val == State.OCCUPIED) {
        count++;
      }
    }
    return count;
  }

  private State scan(Grid<State> grid, int row, int col, Vec2 dir) {
    while (true) {
      row += dir.getY();
      col += dir.getX();
      State val = grid.get(row, col);
      switch (val) {
        case OUTSIDE:
        case EMPTY:
        case OCCUPIED:
          return val;
      }
    }
  }
}
