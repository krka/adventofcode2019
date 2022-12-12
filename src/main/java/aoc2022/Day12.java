package aoc2022;

import util.BFS;
import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day12 implements Day {

  private final Grid<Character> grid;

  public Day12(String name) {
    grid = Grid.from(Util.readResource(name), 'X', Function.identity());
  }

  @Override
  public long solvePart1() {
    final Grid.Entry<Character> s = grid.stream().filter(e -> e.getValue() == 'S').findAny().get();
    Vec2 start = Vec2.of(s.getCol(), s.getRow());
    grid.set(start, 'a');
    final Grid.Entry<Character> e2 = grid.stream().filter(e -> e.getValue() == 'E').findAny().get();
    Vec2 dest = Vec2.of(e2.getCol(), e2.getRow());
    grid.set(dest, 'z');

    List<Vec2> starts = List.of(start);

    return bfs(dest, starts);
  }

  private long bfs(Vec2 dest, List<Vec2> starts) {
    final BFS<Vec2> bfs = BFS.newBFS(Vec2.class)
            .withStarts(starts)
            .withTarget(dest::equals)
            .withEdgeFunction(pos -> Vec2.DIRS.stream().map(pos::add).filter(newPos -> grid.get(newPos) - grid.get(pos) <= 1))
            .build();

    return bfs.run().getSteps();
  }

  @Override
  public long solvePart2() {
    final Grid.Entry<Character> s = grid.stream().filter(e -> e.getValue() == 'S').findAny().get();
    Vec2 start = Vec2.of(s.getCol(), s.getRow());
    grid.set(start, 'a');
    final Grid.Entry<Character> e2 = grid.stream().filter(e -> e.getValue() == 'E').findAny().get();
    Vec2 dest = Vec2.of(e2.getCol(), e2.getRow());
    grid.set(dest, 'z');

    List<Vec2> starts = grid.stream()
            .filter(e -> e.getValue() == 'a')
            .map(Grid.Entry::getPos)
            .collect(Collectors.toList());

    return bfs(dest, starts);
  }
}


