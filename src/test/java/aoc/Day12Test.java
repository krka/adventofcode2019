package aoc;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Day12Test {
  @Test
  public void testPart1() throws IOException {
    Day12 day12 = new Day12("day12.in");
    assertEquals(14606, day12.solve());
  }

  @Test
  public void testPart2Sample2() throws IOException {
    Day12 day12 = new Day12("day12-sample2.in");
    assertEquals(4686774924L, day12.part2());
  }

  @Test
  public void testPart2() throws IOException {
    Day12 day12 = new Day12("day12.in");
    assertEquals(543673227860472L, day12.part2());
  }
}
