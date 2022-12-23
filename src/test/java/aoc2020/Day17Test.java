package aoc2020;

import org.junit.Test;
import util.Grid;
import util.Util;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day17Test {

  public static final String DAY = "17";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(218, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(112, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(1908, solvePart2(MAIN_INPUT));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(848, solvePart2(SAMPLE_INPUT));
  }

  enum State {
    ACTIVE("#"), INACTIVE(".");

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
    Grid<State> inGrid = Grid.from(input, State.INACTIVE, character -> character == '#' ? State.ACTIVE : State.INACTIVE);

    int cycles = 6;
    int maxRows = inGrid.rows() + 2 * cycles;
    int maxCols = inGrid.cols() + 2 * cycles;
    int maxZ = 1 + 2 * cycles;
    Grid<State>[] grids = new Grid[maxZ];
    for (int i = 0; i < maxZ; i++) {
      grids[i] = Grid.create(maxRows, maxCols, State.INACTIVE);
    }
    Grid<State> middlegrid = grids[cycles];
    inGrid.forEach((row, col, value) -> {
      middlegrid.set(row + cycles, col + cycles, value);
    });

    for (int cycle = 0; cycle < cycles; cycle++) {
      Grid<State>[] nextGrids = new Grid[maxZ];
      for (int z = 0; z < maxZ; z++) {
        nextGrids[z] = grids[z].duplicate();
      }

      for (int z = 0; z < maxZ; z++) {
        for (int r = 0; r < maxRows; r++) {
          for (int c = 0; c < maxCols; c++) {
            int neighbours = neighbours(grids, z, r, c);
            if (grids[z].get(r, c) == State.ACTIVE) {
              if (neighbours != 2 && neighbours != 3) {
                nextGrids[z].set(r, c, State.INACTIVE);
              }
            } else {
              if (neighbours == 3) {
                nextGrids[z].set(r, c, State.ACTIVE);
              }
            }
          }
        }
      }

      grids = nextGrids;
    }

    return countActive(grids);
  }

  private long countActive(Grid<State>[] grids) {
    int sum = 0;
    for (int i = 0; i < grids.length; i++) {
      sum += grids[i].count(state -> state == State.ACTIVE);
    }
    return sum;
  }

  private int neighbours(Grid<State>[] grids, int z, int r, int c) {
    int sum = 0;
    for (int dz = -1; dz <= 1; dz++) {
      for (int dr = -1; dr <= 1; dr++) {
        for (int dc = -1; dc <= 1; dc++) {
          if (dz == 0 && dr == 0 && dc == 0) {
            continue;
          }
          int zol = z + dz;
          if (zol >= 0 && zol < grids.length) {
            int row = r + dr;
            int col = c + dc;
            if (grids[zol].get(row, col) == State.ACTIVE) {
              sum++;
            }

          }
        }
      }
    }
    return sum;
  }

  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);
    Grid<State> inGrid = Grid.from(input, State.INACTIVE, character -> character == '#' ? State.ACTIVE : State.INACTIVE);

    int cycles = 6;
    int maxRows = inGrid.rows() + 2 * cycles;
    int maxCols = inGrid.cols() + 2 * cycles;
    int maxZ = 1 + 2 * cycles;
    int maxW = 1 + 2 * cycles;
    Grid<State>[][] grids = new Grid[maxZ][maxW];
    for (int z = 0; z < maxZ; z++) {
      for (int w = 0; w < maxW; w++) {
        grids[z][w] = Grid.create(maxRows, maxCols, State.INACTIVE);
      }
    }
    Grid<State> middlegrid = grids[cycles][cycles];
    inGrid.forEach((row, col, value) -> {
      middlegrid.set(row + cycles, col + cycles, value);
    });

    for (int cycle = 0; cycle < cycles; cycle++) {
      Grid<State>[][] nextGrids = new Grid[maxZ][maxZ];
      for (int z = 0; z < maxZ; z++) {
        for (int w = 0; w < maxW; w++) {
          nextGrids[z][w] = grids[z][w].duplicate();
        }
      }

      for (int z = 0; z < maxZ; z++) {
        for (int w = 0; w < maxZ; w++) {
          for (int r = 0; r < maxRows; r++) {
            for (int c = 0; c < maxCols; c++) {
              int neighbours = neighbours2(grids, z, r, c, w);
              if (grids[z][w].get(r, c) == State.ACTIVE) {
                if (neighbours != 2 && neighbours != 3) {
                  nextGrids[z][w].set(r, c, State.INACTIVE);
                }
              } else {
                if (neighbours == 3) {
                  nextGrids[z][w].set(r, c, State.ACTIVE);
                }
              }
            }
          }
        }
      }

      grids = nextGrids;
    }

    return countActive2(grids);
  }

  private long countActive2(Grid<State>[][] grids) {
    int sum = 0;
    for (int i = 0; i < grids.length; i++) {
      for (int j = 0; j < grids.length; j++) {
        sum += grids[i][j].count(state -> state == State.ACTIVE);
      }
    }
    return sum;
  }

  private int neighbours2(Grid<State>[][] grids, int z, int r, int c, int w) {
    int sum = 0;
    for (int dz = -1; dz <= 1; dz++) {
      for (int dr = -1; dr <= 1; dr++) {
        for (int dc = -1; dc <= 1; dc++) {
          for (int dw = -1; dw <= 1; dw++) {
            if (dz == 0 && dr == 0 && dc == 0 && dw == 0) {
              continue;
            }
            int zol = z + dz;
            int wol = w + dw;
            if (zol >= 0 && zol < grids.length && wol >= 0 && wol < grids[0].length) {
              int row = r + dr;
              int col = c + dc;
              if (grids[zol][wol].get(row, col) == State.ACTIVE) {
                sum++;
              }
            }
          }
        }
      }
    }
    return sum;
  }


}
