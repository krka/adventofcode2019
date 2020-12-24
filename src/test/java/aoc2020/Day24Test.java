package aoc2020;

import org.junit.Test;
import util.Util;
import util.Vec2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Day24Test {

  public static final String DAY = "24";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(450, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(10, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(0, solvePart2(MAIN_INPUT));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(2208, solvePart2(SAMPLE_INPUT));
  }

  private long solvePart1(String name) {
    Map<Vec2, Boolean> grid = initGrid(name);
    return grid.values().stream().filter(a -> a).count();
  }

  private Map<Vec2, Boolean> initGrid(String name) {
    List<String> input = Util.readResource(name);
    Map<Vec2, Boolean> grid = new HashMap<>();
    for (String s : input) {
      int x = 0;
      int y = 0;
      char[] chars = s.toCharArray();
      int i = 0;
      while (i < chars.length) {
        char c = chars[i++];
        switch (c) {
          case 'e': x++; break;
          case 'w': x--; break;
          case 's': {
            char c2 = chars[i++];
            switch (c2) {
              case 'e': x++; y++; break;
              case 'w': y++; break;
              default: throw new RuntimeException();
            }
            break;
          }
          case 'n': {
            char c2 = chars[i++];
            switch (c2) {
              case 'e': y--; break;
              case 'w': x--; y--; break;
              default: throw new RuntimeException();
            }
            break;
          }
          default: throw new RuntimeException();
        }
      }
      grid.merge(Vec2.of(x, y), Boolean.TRUE, (a, b) -> !a);
    }
    return grid;
  }


  private long solvePart2(String name) {
    Map<Vec2, Boolean> grid = initGrid(name);
    for (int i = 0; i < 100; i++) {
      int maxX = (int) grid.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).mapToLong(Vec2::getX).max().getAsLong();
      int minX = (int) grid.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).mapToLong(Vec2::getX).min().getAsLong();
      int maxY= (int) grid.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).mapToLong(Vec2::getY).max().getAsLong();
      int minY = (int) grid.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).mapToLong(Vec2::getY).min().getAsLong();

      Map<Vec2, Boolean> newGrid = new HashMap<>();
      for (int j = minX - 1; j <= maxX + 1; j++) {
        for (int k = minY - 1; k <= maxY + 1; k++) {
          int a = adj(grid, j, k);
          boolean cur = grid.getOrDefault(Vec2.of(j, k), false);
          boolean newcolor;
          if (cur) {
            newcolor = a == 1 || a == 2;
          } else {
            newcolor = a == 2;
          }
          newGrid.put(Vec2.of(j, k), newcolor);
        }
      }
      grid = newGrid;
    }
    return grid.values().stream().filter(a -> a).count();
  }

  private int adj(Map<Vec2, Boolean> grid, int j, int k) {
    int sum = 0;
    sum += grid.getOrDefault(Vec2.of(j - 1, k), false) ? 1 : 0;
    sum += grid.getOrDefault(Vec2.of(j + 1, k), false) ? 1 : 0;
    sum += grid.getOrDefault(Vec2.of(j, k - 1), false) ? 1 : 0;
    sum += grid.getOrDefault(Vec2.of(j, k + 1), false) ? 1 : 0;
    sum += grid.getOrDefault(Vec2.of(j - 1, k - 1), false) ? 1 : 0;
    sum += grid.getOrDefault(Vec2.of(j + 1, k + 1), false) ? 1 : 0;
    return sum;
  }


}
