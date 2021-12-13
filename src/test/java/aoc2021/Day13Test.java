package aoc2021;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day13Test {

  private Day13 day = new Day13("2021/day13.in");

  @Test
  public void testPart1() {
    assertEquals(755, day.solvePart1());
  }

  @Test
  public void testPart2() {
    List<String> expected = List.of(
            "###  #    #  #   ## ###  ###   ##   ## ",
            "#  # #    # #     # #  # #  # #  # #  #",
            "###  #    ##      # #  # ###  #  # #   ",
            "#  # #    # #     # ###  #  # #### # ##",
            "#  # #    # #  #  # # #  #  # #  # #  #",
            "###  #### #  #  ##  #  # ###  #  #  ###"
    );
    // Output is BLKJRBAG
    assertEquals(expected, day.solvePart2());
  }

}