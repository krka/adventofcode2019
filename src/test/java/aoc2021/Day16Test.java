package aoc2021;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day16Test {

  private Day day = Day16.fromResource("2021/day16.in");

  @Test
  public void testPart1() {
    assertEquals(860, day.solvePart1());
  }

  @Test
  public void testPart2() {
    long t1 = System.nanoTime();
    long actual = day.solvePart2();
    long t2 = System.nanoTime();
    System.out.println(t2 - t1);
    assertEquals(470949537659L, actual);
  }

}