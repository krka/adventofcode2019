package aoc2019;

import aoc2019.Day11;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Day11Test {
  @Test
  public void testPart1() {
    assertEquals(1863, new Day11("2019/day11.in").solve(false).size());
  }

  @Test
  public void testPart2() {
    Map<String, Integer> map = new Day11("2019/day11.in").solve(true);
    int minx = Integer.MAX_VALUE;
    int miny = Integer.MAX_VALUE;
    int maxx = Integer.MIN_VALUE;
    int maxy = Integer.MIN_VALUE;
    for (String s : map.keySet()) {
      String[] parts = s.split(",");
      int x = Integer.parseInt(parts[0]);
      int y = Integer.parseInt(parts[1]);
      minx = Math.min(minx, x);
      miny = Math.min(miny, y);
      maxx = Math.max(maxx, x);
      maxy = Math.max(maxy, y);
    }

    int rows = maxy - miny + 1;
    int width = maxx - minx + 1;
    StringBuilder[] output = new StringBuilder[rows];
    for (int i = 0; i < rows; i++) {
      output[i] = new StringBuilder();
      for (int j = 0; j < width; j++) {
        output[i].append(' ');
      }
    }
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      if (entry.getValue() == 1) {
        String coord = entry.getKey();
        String[] parts = coord.split(",");
        int x = Integer.parseInt(parts[0]) - minx;
        int y = Integer.parseInt(parts[1]) - miny;
        output[y].setCharAt(x, '#');
      }
    }

    // Should look like "BLULZJLZ"
    for (StringBuilder sb : output) {
      System.out.println(sb.toString());
    }
  }
}
