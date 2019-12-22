package aoc;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class Day22Test {
  @Test
  public void testPart1Sample1() {
    Day22 day22 = new Day22("day22-sample1.in");
    Integer[] res = new Integer[10];
    for (int i = 0; i < 10; i++) {
      int newPosition = (int) day22.part1(i, 10);
      res[newPosition] = i;
    }
    assertEquals(Arrays.asList(0, 3, 6, 9, 2, 5, 8, 1, 4, 7), Arrays.asList(res));
  }

  @Test
  public void testPart1Sample2() {
    Day22 day22 = new Day22("day22-sample2.in");
    Integer[] res = new Integer[10];
    for (int i = 0; i < 10; i++) {
      int newPosition = (int) day22.part1(i, 10);
      res[newPosition] = i;
    }
    assertEquals(Arrays.asList(3, 0, 7, 4, 1, 8, 5, 2, 9, 6), Arrays.asList(res));
  }

  @Test
  public void testPart1() {
    assertEquals(2322, new Day22("day22.in").part1(2019, 10007));
  }

  @Test
  public void testPart2() {
    assertEquals(49283089762689L, new Day22("day22.in").part2(119315717514047L, 101741582076661L, 2020));
  }
}