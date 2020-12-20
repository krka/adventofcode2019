package aoc2020;

import org.junit.Test;
import util.Grid;
import util.Rotatable;
import util.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Day20Test {

  public static final String DAY = "20";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(7901522557967L, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(20899048083289L, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(2476, solvePart2(MAIN_INPUT));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(273, solvePart2(SAMPLE_INPUT));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    List<Tile> tiles = input.stream()
            .collect(Util.splitBy(String::isEmpty))
            .stream().map(Tile::new)
            .collect(Collectors.toList());

    Map<Integer, Set<Tile>> edgeMap = createEdgeMap(tiles);

    return tiles.stream().filter(tile -> tile.isCorner(edgeMap))
            .map(tile -> tile.id)
            .mapToLong(Integer::longValue)
            .reduce((a, b) -> a * b)
            .getAsLong();
  }


  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);
    List<Tile> tiles = input.stream()
            .collect(Util.splitBy(String::isEmpty))
            .stream().map(Tile::new)
            .collect(Collectors.toList());

    Map<Integer, Set<Tile>> edgeMap = createEdgeMap(tiles);

    List<Tile> allCorners = tiles.stream()
            .filter(tile -> tile.isCorner(edgeMap))
            .collect(Collectors.toList());

    assertEquals(4, allCorners.size());

    // Doesn't matter which corner we choose
    Tile corner = allCorners.get(0);

    Set<Integer> cornerEdges = corner.countOnes(edgeMap);
    assertEquals(4, cornerEdges.size());

    int dim = (int) Math.sqrt(tiles.size());
    Grid<Tile> tileGrid = new Grid<>(dim, dim, null);

    corner = Rotatable.rotateUntil(corner, tile -> cornerEdges.contains(tile.leftCol) && cornerEdges.contains(tile.topRow));
    tileGrid.set(0, 0, corner);

    for (int col = 1; col < dim; col++) {
      Tile prev = tileGrid.get(0, col - 1);
      Tile match = getAdjacent(edgeMap, prev, prev.rightCol);
      match = Rotatable.rotateUntil(match, tile -> tile.leftCol == prev.rightCol);
      tileGrid.set(0, col, match);
    }

    for (int row = 1; row < dim; row++) {
      Tile prev = tileGrid.get(row - 1, 0);
      Tile match = getAdjacent(edgeMap, prev, prev.bottomRow);
      match = Rotatable.rotateUntil(match, tile -> tile.topRow == prev.bottomRow);
      tileGrid.set(row, 0, match);
    }

    for (int row = 1; row < dim; row++) {
      for (int col = 1; col < dim; col++) {
        Tile left = tileGrid.get(row, col - 1);
        Tile above = tileGrid.get(row - 1, col);
        int targetLeft = left.rightCol;
        int targetAbove = above.bottomRow;

        Tile match = getAdjacent(edgeMap, left, targetLeft);
        match = Rotatable.rotateUntil(match, tile -> tile.leftCol == targetLeft);
        assertEquals(match.topRow, targetAbove);
        tileGrid.set(row, col, match);
      }
    }

    int size = tiles.get(0).grid.cols() - 2;

    Grid<Character> grid = new Grid<>(size * dim, size * dim, 'X');
    for (int r = 0; r < dim; r++) {
      for (int c = 0; c < dim; c++) {
        grid.setGrid(r * size, c * size, tileGrid.get(r, c).grid, 1, 1, size, size);
      }
    }

    Grid<Character> monster = Grid.from(
            List.of(
                    "                  # ",
                    "#    ##    ##    ###",
                    " #  #  #  #  #  #   "
            ), 'X', c -> c);

    return Rotatable.all(grid)
            .mapToLong(g -> countMonsters(monster, g))
            .filter(v -> v >= 0)
            .reduce(Util.exactlyOneLong()).getAsLong();
  }

  private long countMonsters(Grid<Character> monster, Grid<Character> grid) {
    boolean hasMonster = false;
    Grid<Integer> monsterCount = new Grid<>(grid.rows(), grid.cols(), 0);
    for (int row = 0; row < grid.rows() - monster.rows(); row++) {
      for (int col = 0; col < grid.cols() - monster.cols(); col++) {
        if (isMonster(grid, row, col, monster)) {
          markMonster(monsterCount, monster, row, col);
          hasMonster = true;
        }
      }
    }
    if (hasMonster) {
      return grid.count(v -> v == '#') - monsterCount.count(v1 -> v1 == 1);
    }
    return -1;
  }

  private Tile getAdjacent(Map<Integer, Set<Tile>> edgeMap, Tile prev, int edge) {
    Set<Tile> matches = edgeMap.get(edge);
    assertEquals(2, matches.size());
    return matches.stream()
            .filter(tile -> tile.id != prev.id)
            .reduce(Util.exactlyOne()).get();
  }

  private Map<Integer, Set<Tile>> createEdgeMap(List<Tile> tiles) {
    Map<Integer, Set<Tile>> counts = new HashMap<>();
    for (Tile tile : tiles) {
      addSignature(counts, tile, tile.topRow);
      addSignature(counts, tile, tile.bottomRow);
      addSignature(counts, tile, tile.leftCol);
      addSignature(counts, tile, tile.rightCol);
    }
    for (Set<Tile> value : counts.values()) {
      int size = value.size();
      switch (size) {
        case 1: break; // Frame piece
        case 2: break; // middle piece
        default: fail("Unexpected size: " + size);
      }
    }
    return counts;
  }

  private void addSignature(Map<Integer, Set<Tile>> counts, Tile tile, int add) {
    counts.computeIfAbsent(add, ignore -> new HashSet<>()).add(tile);
    counts.computeIfAbsent(rev(add), ignore -> new HashSet<>()).add(tile);
  }

  private void markMonster(Grid<Integer> monsterCount, Grid<Character> monster2, int row, int col) {
    for (int mr = 0; mr < monster2.rows(); mr++) {
      for (int mc = 0; mc < monster2.cols(); mc++) {
        if (monster2.get(mr, mc) == '#') {
          monsterCount.set(row + mr, col + mc, 1);
        }
      }
    }
  }

  private boolean isMonster(Grid<Character> grid, int row, int col, Grid<Character> monster) {
    for (int mr = 0; mr < monster.rows(); mr++) {
      for (int mc = 0; mc < monster.cols(); mc++) {
        if (monster.get(mr, mc) == '#' && grid.get(row + mr, col + mc) != '#') {
          return false;
        }
      }
    }
    return true;
  }

  private static int rev(int edge) {
    return Integer.reverse(edge) >>> 22;
  }

  static class Tile implements Rotatable {
    final Grid<Character> grid;
    final int id;
    int topRow;
    int bottomRow;
    int leftCol;
    int rightCol;

    public Tile(Grid<Character> grid, int id, int topRow, int bottomRow, int leftCol, int rightCol) {
      this.grid = grid;
      this.id = id;
      this.topRow = topRow;
      this.bottomRow = bottomRow;
      this.leftCol = leftCol;
      this.rightCol = rightCol;
    }

    Tile(List<String> data) {
      id = Integer.parseInt(data.get(0).split("[ :]+")[1]);
      grid = Grid.from(data.subList(1, data.size()), 'X', c -> c);

      for (int col = 0; col < grid.cols(); col++) {
        topRow <<= 1;
        topRow |= grid.get(0, col) == '.' ? 0 : 1;

        bottomRow <<= 1;
        bottomRow |= grid.get(grid.rows() - 1, col) == '.' ? 0 : 1;
      }

      for (int row = 0; row < grid.rows(); row++) {
        leftCol <<= 1;
        leftCol |= grid.get(row, 0) == '.' ? 0 : 1;

        rightCol <<= 1;
        rightCol |= grid.get(row, grid.cols() - 1) == '.' ? 0 : 1;
      }
    }

    public boolean isCorner(Map<Integer, Set<Tile>> edges) {
      return countOnes(edges).size() == 4;
    }

    Set<Integer> countOnes(Map<Integer, Set<Tile>> edgeMap) {
      Set<Integer> res = new HashSet<>();
      addIfOne(res, edgeMap, topRow);
      addIfOne(res, edgeMap, bottomRow);
      addIfOne(res, edgeMap, leftCol);
      addIfOne(res, edgeMap, rightCol);
      return res;
    }

    private void addIfOne(Set<Integer> res, Map<Integer, Set<Tile>> edgeMap, int signature) {
      if (edgeMap.getOrDefault(signature, Set.of()).size() == 1) {
        res.add(signature);
        res.add(rev(signature));
      }
    }

    @Override
    public Tile rotateLeft() {
      return new Tile(grid.rotateLeft(), id, rightCol, leftCol, rev(topRow), rev(bottomRow));
    }

    @Override
    public Tile mirror() {
      return new Tile(grid.mirror(), id, bottomRow, topRow, rev(leftCol), rev(rightCol));
    }

  }

}
