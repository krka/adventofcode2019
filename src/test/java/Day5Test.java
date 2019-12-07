import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Day5Test {
  @Test
  public void testPart1() throws Exception {
    List<Integer> expected = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 16348437);

    assertEquals(expected, Day5.part1());
  }

  @Test
  public void testPart2() throws Exception {
    List<Integer> expected = Arrays.asList(6959377);

    assertEquals(expected, Day5.part2());
  }
}