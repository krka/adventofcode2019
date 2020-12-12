package aoc2020;

import org.junit.Test;
import util.Util;
import util.Vec2;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day12Test {

  public static final String DAY = "12";
  public static final String MAIN_INPUT = "2020/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = "2020/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(1148, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(25, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(52203, solvePart2(MAIN_INPUT));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(286, solvePart2(SAMPLE_INPUT));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    Vec2 pos = Vec2.zero();
    Vec2 dir = Vec2.of(1, 0);
    for (String s : input) {
      char type = s.charAt(0);
      int n = Integer.parseInt(s.substring(1));
      switch (type) {
        case 'F': pos = pos.add(dir.multiply(n)); break;
        case 'L':
        case 'R':
          dir = dir.rotateRight(((n / 90) * (type == 'L' ? 3 : 1)) % 4);
          break;
        case 'W': pos = pos.add(Vec2.WEST.multiply(n)); break;
        case 'E': pos = pos.add(Vec2.EAST.multiply(n)); break;
        case 'S': pos = pos.add(Vec2.SOUTH.multiply(n)); break;
        case 'N': pos = pos.add(Vec2.NORTH.multiply(n)); break;
        default: throw new RuntimeException();
      }
    }
    return pos.manhattan();
  }

  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);

    Vec2 pos = Vec2.zero();
    Vec2 dir = Vec2.of(10, -1);
    for (String s : input) {
      char type = s.charAt(0);
      int n = Integer.parseInt(s.substring(1));
      switch (type) {
        case 'F': pos = pos.add(dir.multiply(n)); break;
        case 'L':
        case 'R':
          dir = dir.rotateRight(((n / 90) * (type == 'L' ? 3 : 1)) % 4);
          break;
        case 'W': dir = dir.add(Vec2.WEST.multiply(n)); break;
        case 'E': dir = dir.add(Vec2.EAST.multiply(n)); break;
        case 'S': dir = dir.add(Vec2.SOUTH.multiply(n)); break;
        case 'N': dir = dir.add(Vec2.NORTH.multiply(n)); break;
        default: throw new RuntimeException();
      }
    }
    return pos.manhattan();
  }

}
