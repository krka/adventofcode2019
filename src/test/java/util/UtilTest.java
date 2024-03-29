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
    assertAdj(List.of(List.of(1, 2)), 1, 2);
    assertAdj(List.of(List.of(1, 2), List.of(2, 3)), 1, 2, 3);
  }

  private void assertAdj(List<List<Integer>> expected, int... input) {
    List<List<Integer>> actual = IntStream.of(input).boxed().collect(Util.windows(2));
    assertEquals(expected, actual);
  }

  @Test
  public void testFactors() {
    assertEquals(List.of(0L), Util.factors(0));
    assertEquals(List.of(1L), Util.factors(1));
    assertEquals(List.of(2L), Util.factors(2));
    assertEquals(List.of(2L, 2L), Util.factors(4));
    assertEquals(List.of(7369L, 7369L), Util.factors(7369 * 7369));
  }
}