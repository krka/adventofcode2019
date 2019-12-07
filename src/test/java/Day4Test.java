import org.junit.Test;

import static org.junit.Assert.*;

public class Day4Test {
  private static final int START = 236491;
  private static final int END = 713787;

  @Test
  public void testPart1() {
    assertEquals(1169, Day4.part1(START, END));
  }

  @Test
  public void testPart2() {
    assertEquals(757, Day4.part2(START, END));
  }
}