package aoc2020;

import org.junit.Test;
import util.Grid;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

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

    Set<Integer> edgeSignatures = edgeSignature(tiles);

    return tiles.stream().filter(tile -> tile.hasTwoEdges(edgeSignatures))
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

    Set<Integer> edgeSignatures = edgeSignature(tiles);

    int dim = (int) Math.sqrt(tiles.size());

    Grid<Tile> tileGrid = new Grid<>(dim, dim, null);

    // Doesn't matter which corner we choose
    Tile corner = tiles.stream().filter(tile -> tile.hasTwoEdges(edgeSignatures)).findFirst().get();

    Set<Integer> used = new HashSet<>();
    used.add(corner.id);

    List<Integer> ones = corner.getOnes(edgeSignatures);
    corner = rotateUntilMatch(corner, tile -> ones.contains(tile.leftCol) && ones.contains(tile.topRow));
    tileGrid.set(0, 0, corner);

    for (int i = 1; i < dim; i++) {
      int targetEdge = tileGrid.get(0, i - 1).rightCol;
      Tile match = tiles.stream()
              .filter(tile -> !used.contains(tile.id))
              .filter(tile -> tile.matches(targetEdge))
              .reduce(Util.exactlyOne()).get();

      match = rotateUntilMatch(match, tile -> tile.leftCol == targetEdge && edgeSignatures.contains(tile.topRow));
      used.add(match.id);
      tileGrid.set(0, i, match);
    }

    for (int i = 1; i < dim; i++) {
      int targetEdge = tileGrid.get(i - 1, 0).bottomRow;
      Tile match = tiles.stream()
              .filter(tile -> !used.contains(tile.id))
              .filter(tile -> tile.matches(targetEdge))
              .reduce(Util.exactlyOne()).get();
      match = rotateUntilMatch(match, tile -> tile.topRow == targetEdge && edgeSignatures.contains(tile.leftCol));
      used.add(match.id);
      tileGrid.set(i, 0, match);
    }

    for (int row = 1; row < dim; row++) {
      for (int col = 1; col < dim; col++) {
        int targetLeft = tileGrid.get(row, col - 1).rightCol;
        int targetTop = tileGrid.get(row - 1, col).bottomRow;
        Tile match = tiles.stream()
                .filter(tile -> !used.contains(tile.id))
                .filter(tile -> tile.matches(targetLeft) && tile.matches(targetTop))
                .reduce(Util.exactlyOne()).get();
        match = rotateUntilMatch(match, tile -> tile.topRow == targetTop && tile.leftCol == targetLeft);
        used.add(match.id);
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

    for (int mirror = 0; mirror < 2; mirror++) {
      for (int rot = 0; rot < 4; rot++) {
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
        grid = grid.rotateLeft();
      }
      grid = grid.mirror();
    }

    throw new RuntimeException();
  }

  private Set<Integer> edgeSignature(List<Tile> tiles) {
    Map<Integer, Integer> counts = new HashMap<>();
    for (Tile tile : tiles) {
      counts.merge(tile.topRow, 1, Integer::sum);
      counts.merge(tile.bottomRow, 1, Integer::sum);
      counts.merge(tile.leftCol, 1, Integer::sum);
      counts.merge(tile.rightCol, 1, Integer::sum);
      counts.merge(tile.topRev, 1, Integer::sum);
      counts.merge(tile.bottomRev, 1, Integer::sum);
      counts.merge(tile.leftRev, 1, Integer::sum);
      counts.merge(tile.rightRev, 1, Integer::sum);
    }
    return counts.entrySet().stream()
            .filter(entry -> entry.getValue() == 1)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
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

  private Tile rotateUntilMatch(Tile tile, Predicate<Tile> predicate) {
    for (int mirror = 0; mirror < 2; mirror++) {
      for (int rot = 0; rot < 4; rot++) {
        if (predicate.test(tile)) {
          return tile;
        }
        tile = tile.rotateLeft();
      }
      tile = tile.mirror();
    }
    throw new RuntimeException();
  }

  static class Tile {
    final Grid<Character> grid;
    final int id;
    int topRow;
    int bottomRow;
    int leftCol;
    int rightCol;
    int topRev;
    int bottomRev;
    int leftRev;
    int rightRev;

    public Tile(Grid<Character> grid, int id, int topRow, int bottomRow, int leftCol, int rightCol) {
      this.grid = grid;
      this.id = id;
      this.topRow = topRow;
      this.bottomRow = bottomRow;
      this.leftCol = leftCol;
      this.rightCol = rightCol;
      this.topRev = rev(topRow);
      this.bottomRev = rev(bottomRow);
      this.leftRev = rev(leftCol);
      this.rightRev = rev(rightCol);
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

      topRev = rev(topRow);
      bottomRev = rev(bottomRow);
      leftRev = rev(leftCol);
      rightRev = rev(rightCol);
    }

    private int rev(int edge) {
      return Integer.reverse(edge) >>> 22;
    }

    boolean matches(int edge) {
      return edge == topRow || edge == bottomRow || edge == leftCol || edge == rightCol ||
              edge == topRev || edge == bottomRev || edge == leftRev || edge == rightRev;
    }

    List<Integer> getOnes(Set<Integer> edgeSignatures) {
      ArrayList<Integer> res = new ArrayList<>();
      addIfOne(res, edgeSignatures, topRow);
      addIfOne(res, edgeSignatures, topRev);
      addIfOne(res, edgeSignatures, bottomRow);
      addIfOne(res, edgeSignatures, bottomRev);
      addIfOne(res, edgeSignatures, leftCol);
      addIfOne(res, edgeSignatures, leftRev);
      addIfOne(res, edgeSignatures, rightCol);
      addIfOne(res, edgeSignatures, rightRev);
      return res;
    }

    private void addIfOne(ArrayList<Integer> res, Set<Integer> edgeSignatures, int signature) {
      if (edgeSignatures.contains(signature)) {
        res.add(signature);
      }
    }

    public boolean hasTwoEdges(Set<Integer> edges) {
      return getOnes(edges).size() == 4;
    }

    public Tile rotateLeft() {
      return new Tile(grid.rotateLeft(), id, rightCol, leftCol, topRev, bottomRev);
    }

    public Tile mirror() {
      return new Tile(grid.mirror(), id, bottomRow, topRow, leftRev, rightRev);
    }
  }

}
