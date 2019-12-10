package aoc;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class Day10Test {
  @Test
  public void testSample1() {
    assertEquals(8, new Day10("day10-sample1.in").solve());
  }

  @Test
  public void testSample2() {
    assertEquals(33, new Day10("day10-sample2.in").solve());
  }

  @Test
  public void testSample3() {
    assertEquals(35, new Day10("day10-sample3.in").solve());
  }

  @Test
  public void testSample4() {
    assertEquals(41, new Day10("day10-sample4.in").solve());
  }

  @Test
  public void testSample5() {
    assertEquals(210, new Day10("day10-sample5.in").solve());
  }

  @Test
  public void testPart1() {
    assertEquals(319, new Day10("day10.in").solve());
  }

  @Test
  public void testPart2Sample() {
    List<Day10.Point> points = new Day10("day10-part2-sample.in").getOrdering(8, 3);
    printPoints(points);
  }

  @Test
  public void testPart2Sample2() {
    List<Day10.Point> points = new Day10("day10-sample5.in").getOrdering(11, 13);
    printPoints(points);
  }

  @Test
  public void testPart2() {
    // Row and col from part 1
    List<Day10.Point> points = new Day10("day10.in").getOrdering(31, 20);
    printPoints(points);

    Day10.Point point = points.get(199);
    int answer = point.col * 100 + point.row;

    assertEquals(517, answer);
  }

  private void printPoints(List<Day10.Point> points) {
    for (int i = 0; i < points.size(); i++) {
      Day10.Point point = points.get(i);
      System.out.printf("%d: %d, %d%n", i + 1, point.col, point.row);
    }
  }

}