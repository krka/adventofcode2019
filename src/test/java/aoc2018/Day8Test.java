package aoc2018;

import org.junit.Test;
import util.Util;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Day8Test {

  public static final String DAY = "8";
  public static final String YEAR = "2018";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(43351, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(21502, solvePart2(MAIN_INPUT));
  }

  private int solvePart1(String name) {
    List<String> input = Util.readResource(name);

    return node1(input.stream().map(s -> s.split(" ")).flatMap(Stream::of).mapToInt(Integer::parseInt).boxed().iterator());
  }

  private int node1(Iterator<Integer> iter) {
    int numChildren = iter.next();
    int numMetadata = iter.next();

    int sum = 0;
    for (int i = 0; i < numChildren; i++) {
      sum += node1(iter);
    }
    for (int i = 0; i < numMetadata; i++) {
      sum += iter.next();
    }

    return sum;
  }

  private int solvePart2(String name) {
    List<String> input = Util.readResource(name);

    return node2(input.stream().map(s -> s.split(" ")).flatMap(Stream::of).mapToInt(Integer::parseInt).boxed().iterator());
  }

  private int node2(Iterator<Integer> iter) {
    int numChildren = iter.next();
    int numMetadata = iter.next();

    int[] val = new int[numChildren];
    for (int i = 0; i < numChildren; i++) {
      val[i] = node2(iter);
    }

    if (numChildren == 0) {
      int sum = 0;
      for (int i = 0; i < numMetadata; i++) {
        sum += iter.next();
      }
      return sum;
    } else {
      int sum = 0;
      for (int i = 0; i < numMetadata; i++) {
        int index = iter.next() - 1;
        if (0 <= index && index < numChildren) {
          sum += val[index];
        }
      }
      return sum;
    }
  }

}
