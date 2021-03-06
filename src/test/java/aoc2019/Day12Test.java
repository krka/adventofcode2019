package aoc2019;

import aoc2019.Day12;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day12Test {
  @Test
  public void testPart1() {
    Day12 day12 = new Day12("2019/day12.in");
    assertEquals(14606, day12.solve());
  }

  @Test
  public void testPart2Sample2() {
    Day12 day12 = new Day12("2019/day12-sample2.in");
    assertEquals(4686774924L, day12.part2());
  }

  @Test
  public void testPart2() {
    Day12 day12 = new Day12("2019/day12.in");
    assertEquals(543673227860472L, day12.part2());
  }
}
