package aoc2022;

import util.BFS;
import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Day24 implements Day {

  private final Grid<Character> grid;
  private final int cols;
  private final int rows;
  private final int totalPeriod;
  private final List<Grid<Character>> grids;

  public Day24(String name) {
    final List<List<String>> collect = Util.readResource(name).stream().collect(Util.splitBy(s -> s.isEmpty()));
    grid = Grid.from(collect.get(0), '#', Function.identity());
    List<Vec2> up = new ArrayList<>();
    List<Vec2> down = new ArrayList<>();
    List<Vec2> left = new ArrayList<>();
    List<Vec2> right = new ArrayList<>();
    grid.forEach((row, col, value) -> {
      switch (value) {
        case '^': add(up, row, col); break;
        case 'v': add(down, row, col); break;
        case '<': add(left, row, col); break;
        case '>': add(right, row, col); break;
      }
    });
    cols = grid.cols() - 2;
    rows = grid.rows() - 2;

    final int gcd = BigInteger.valueOf(cols).gcd(BigInteger.valueOf(rows)).intValueExact();
    totalPeriod = cols * rows / gcd;

    grids = new ArrayList<>();
    for (int i = 0; i < totalPeriod; i++) {
      final Grid<Character> grid = Grid.create(rows, cols, '.');
      grids.add(grid);
      addAll(up, grid, i, Vec2.NORTH, '^');
      addAll(down, grid, i, Vec2.SOUTH, 'v');
      addAll(left, grid, i, Vec2.WEST, '<');
      addAll(right, grid, i, Vec2.EAST, '>');
    }
  }

  private void addAll(List<Vec2> list, Grid<Character> grid, int i, Vec2 dir, char c) {
    for (Vec2 vec2 : list) {
      long row = Math.floorMod(vec2.getY() + dir.getY() * i, (long) rows);
      long col = Math.floorMod(vec2.getX() + dir.getX() * i, (long) cols);
      grid.set(row, col, c);
    }
  }

  private void add(List<Vec2> list, int row, int col) {
    list.add(Vec2.of(col - 1, row - 1));
  }


  @Override
  public long solvePart1() {
    final BFS.Node<State> run = bfs(new State(-1, 0, 0), state -> state.row == rows).run();
    return run.getSteps();
  }

  @Override
  public long solvePart2() {
    final State start = new State(-1, 0, 0);
    final BFS.Node<State> run1 = bfs(start, state -> state.row == rows).run();
    final BFS.Node<State> run2 = bfs(run1.getPos(), s -> s.row == -1).run();
    final BFS.Node<State> run3 = bfs(run2.getPos(), s -> s.row == rows).run();
    return run1.getSteps() + run2.getSteps() + run3.getSteps();
  }

  public long solveForTarget(long target) {
    int[] steps1 = new int[totalPeriod];
    int[] steps2 = new int[totalPeriod];
    long[] tripsPerPeriod = new long[totalPeriod];
    long[] stepsPerPeriod = new long[totalPeriod];
    long totalTrips = 0;
    long totalSteps = 0;
    int curPeriod = 0;
    while (totalTrips < target) {
      if (steps1[curPeriod] == 0) {
        final BFS.Node<State> run = bfs(new State(-1, 0, curPeriod), state -> state.row == rows).run();
        steps1[curPeriod] = run.getSteps();
        final BFS.Node<State> run2 = bfs(run.getPos(), state -> state.row == -1).run();
        steps2[curPeriod] = run2.getSteps();
      }
      final int addedSteps = steps1[curPeriod] + steps2[curPeriod];
      totalSteps += addedSteps;
      totalTrips++;
      if (tripsPerPeriod[curPeriod] != 0) {
        long diffSteps = totalSteps - stepsPerPeriod[curPeriod];
        long diffTrips = totalTrips - tripsPerPeriod[curPeriod];

        long remaining = target - totalTrips;
        long fullCycles = remaining / diffTrips;
        totalTrips += fullCycles * diffTrips;
        totalSteps += fullCycles * diffSteps;
      } else {
        tripsPerPeriod[curPeriod] = totalTrips;
        stepsPerPeriod[curPeriod] = totalSteps;
      }
      curPeriod = (curPeriod + addedSteps) % totalPeriod;
    }

    if (steps1[curPeriod] == 0) {
      final BFS.Node<State> run = bfs(new State(-1, 0, curPeriod), state -> state.row == rows).run();
      steps1[curPeriod] = run.getSteps();
    }

    return totalSteps + steps1[curPeriod];
  }

  private BFS<State> bfs(State start, Predicate<State> target) {
    return BFS.newBFS(State.class)
              .withStarts(List.of(start))
              .withTarget(target)
              .withEdgeFunction(s -> {
                final ArrayList<State> list = new ArrayList<>();
                final int newPeriod = (s.period + 1) % totalPeriod;
                final Grid<Character> newGrid = grids.get(newPeriod);
                for (Vec2 dir : Vec2.DIRS) {
                  final int newRow = (int) (s.row + dir.getY());
                  final int newCol = (int) (s.col + dir.getX());
                  if (newRow == -1 && newCol == 0 || (newRow == rows && newCol == cols - 1)) {
                    list.add(new State(newRow, newCol, newPeriod));
                  } else if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol
                          < cols) {
                    if ('.' == newGrid.get(newRow, newCol)) {
                      list.add(new State(newRow, newCol, newPeriod));
                    }
                  }
                }
                if ('.' == newGrid.get(s.row, s.col)) {
                  list.add(new State(s.row, s.col, newPeriod));
                }
                return list.stream();
              })
              .build();
  }

  private class State {
    final int row;
    final int col;
    final int period;

    private State(int row, int col, int period) {
      this.row = row;
      this.col = col;
      this.period = period;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      State state = (State) o;
      return row == state.row && col == state.col && period == state.period;
    }

    @Override
    public int hashCode() {
      return Objects.hash(row, col, period);
    }

    @Override
    public String toString() {
      return "State2{" +
              "row=" + row +
              ", col=" + col +
              ", period=" + period +
              '}';
    }
  }
}

