package aoc2023;

import util.BFS;
import util.Day;
import util.Grid;
import util.Pair;
import util.Util;
import util.Vec2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day16 implements Day {
  private static final Map<Pair<Character, Vec2>, List<Vec2>> MAPPING = new HashMap<>();
  static {
    Vec2.DIRS.forEach(dir -> {
      Util.toList("-|\\/.").forEach(ch -> {
        final Pair<Character, Vec2> key = Pair.of(ch, dir);
        MAPPING.put(key, computeMapping(ch, dir));
      });
    });
  }

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

  private long bfs(Vec2 pos, Vec2 dir) {
    final BFS<Pair<Vec2, Vec2>> bfs = BFS.newBFS(Pair.of(pos, dir)).withEdgeFunction(this::next).build();
    bfs.run();
    return bfs.visited().stream().map(Pair::a).distinct().count();
  }

  private Stream<Pair<Vec2, Vec2>> next(Pair<Vec2, Vec2> node) {
    return MAPPING.get(Pair.of(grid.get(node.a()), node.b()))
            .stream().map(dir -> Pair.of(node.a().add(dir), dir))
            .filter(x -> grid.inbound(x.a()));
  }

  private static List<Vec2> computeMapping(char ch, Vec2 dir) {
    switch (ch) {
      case '.': return List.of(dir);
      case '\\': return List.of(dir.rotateLeft(dir.vertical() ? 1 : -1));
      case '/': return List.of(dir.rotateRight(dir.vertical() ? 1 : -1));
      case '|': return dir.vertical() ? List.of(dir) : List.of(Vec2.SOUTH, Vec2.NORTH);
      case '-': return dir.horizontal() ? List.of(dir) : List.of(Vec2.EAST, Vec2.WEST);
      default: throw new RuntimeException();
    }
  }
}

