package aoc2019;

import aoc2019.Day13;
import intcode.IntCode;
import org.junit.Test;
import util.Vector3;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Day13Test {
  @Test
  public void testPart1() {
    IntCode intCode = IntCode.fromResource("2019/day13.in");
    intCode.run();
    Map<Vector3, Integer> map = new HashMap<>();
    List<BigInteger> output = intCode.drainStdout();
    Iterator<BigInteger> iterator = output.iterator();
    while (iterator.hasNext()) {
      int x = iterator.next().intValueExact();
      int y = iterator.next().intValueExact();
      int tile = iterator.next().intValueExact();
      map.put(new Vector3(x, y, 0), tile);
    }
    assertEquals(412, map.values().stream().filter(tile -> tile == 2).count());
  }

  @Test
  public void testPart2() {
    assertEquals(20940, new Day13().solvePart2().intValueExact());
  }

}
