package util;

import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class UtilTest {
  @Test
  public void testPartitions() {
    assertPartitions(List.of());
    assertPartitions(List.of(List.of(1)), 1);
    assertPartitions(List.of(List.of(1, 1)), 1, 1);
    assertPartitions(List.of(List.of(1), List.of(2)), 1, 2);
    assertPartitions(List.of(List.of(1), List.of(2, 2), List.of(5, 5, 5), List.of(7)), 1, 2, 2, 5, 5, 5, 7);
  }

  private void assertPartitions(List<List<Integer>> expected, int... input) {
    List<List<Integer>> actual = IntStream.of(input).boxed().collect(Util.toPartitions(Integer::equals));
    assertEquals(expected, actual);
  }

  @Test
  public void testAdj() {
    assertAdj(List.of());
    assertAdj(List.of(), 1);
    assertAdj(List.of(Pair.of(1, 2)), 1, 2);
    assertAdj(List.of(Pair.of(1, 2), Pair.of(2, 3)), 1, 2, 3);
  }

  private void assertAdj(List<Pair<Integer, Integer>> expected, int... input) {
    List<Pair<Integer, Integer>> actual = IntStream.of(input).boxed().collect(Util.adj());
    assertEquals(expected, actual);
  }
}