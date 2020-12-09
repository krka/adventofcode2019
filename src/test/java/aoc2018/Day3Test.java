package aoc2018;

import org.junit.Test;
import util.Util;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day3Test {

  private static final List<String> input = Util.readResource("2018/day3.in");

  @Test
  public void testPart1() {
    assertEquals(98005, solvePart1());
  }

  private long solvePart1() {
    int[][] grid = new int[1000][1000];
    for (String s : input) {
      String[] s1 = s.trim().split("[ @,:x]+");
      int minx = Integer.parseInt(s1[1]);
      int miny = Integer.parseInt(s1[2]);
      int width = Integer.parseInt(s1[3]);
      int height = Integer.parseInt(s1[4]);
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          grid[minx + i][miny + j]++;
        }
      }
    }
    int count = 0;
    for (int i = 0; i < 1000; i++) {
      for (int j = 0; j < 1000; j++) {
        if (grid[i][j] >= 2) {
          count++;
        }
      }
    }
    return count;
  }

  @Test
  public void testPart2() {
    assertEquals(3353494, solvePart2());
  }

  private long solvePart2() {
    int[][] grid = new int[1000][1000];
    for (String s : input) {
      String[] s1 = s.trim().split("[ @,:x#]+");
      int id = Integer.parseInt(s1[1]);
      int minx = Integer.parseInt(s1[2]);
      int miny = Integer.parseInt(s1[3]);
      int width = Integer.parseInt(s1[4]);
      int height = Integer.parseInt(s1[5]);
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          grid[minx + i][miny + j]++;
        }
      }
    }
    for (String s : input) {
      String[] s1 = s.trim().split("[ @,:x#]+");
      int id = Integer.parseInt(s1[1]);
      int minx = Integer.parseInt(s1[2]);
      int miny = Integer.parseInt(s1[3]);
      int width = Integer.parseInt(s1[4]);
      int height = Integer.parseInt(s1[5]);
      if (isSafe(grid, minx, miny, width, height)) {
        return id;
      }
    }
    throw new RuntimeException();
  }

  private boolean isSafe(int[][] grid, int minx, int miny, int width, int height) {
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (grid[minx + i][miny + j] >= 2) {
          return false;
        }
      }
    }
    return true;
  }

}