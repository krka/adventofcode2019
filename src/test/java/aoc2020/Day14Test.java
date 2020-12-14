package aoc2020;

import org.junit.Test;
import util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Day14Test {

  public static final String DAY = "14";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";
  private static final long MASK36 = (1L << 36) - 1L;

  @Test
  public void testPart1() {
    assertEquals(9628746976360L, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(51, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(4574598714592L, solvePart2(MAIN_INPUT));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(208, solvePart2(SAMPLE_INPUT));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    String mask = null;
    Map<Long, Long> mem = new HashMap<>();
    for (String s : input) {
      String[] split = s.split("[ \\[\\]=]+");
      if (split[0].equals("mask")) {
        mask = split[1];
      } else {
        long addr = Long.parseLong(split[1]);
        long val = Long.parseLong(split[2]);
        mem.put(addr, apply(mask, val));
      }
    }
    return mem.values().stream().mapToLong(s -> s).sum();
  }

  private long apply(String mask, long val) {
    long res = 0;
    for (int i = 0; i < 36; i++) {
      char c = mask.charAt(35 - i);
      if (c == 'X') {
        res |= val & (1L << i);
      } else if (c == '1') {
        res |= 1L << i;
      }
    }
    return res;
  }

  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);
    String mask = null;
    Map<Long, Long> mem = new HashMap<>();
    for (String s : input) {
      String[] split = s.split("[ \\[\\]=]+");
      if (split[0].equals("mask")) {
        mask = split[1];
      } else {
        long addr = Long.parseLong(split[1]);
        long val = Long.parseLong(split[2]);

        rec(mem, mask, val, 0, addr);
      }
    }
    return mem.values().stream().mapToLong(v -> v).sum();
  }

  private void rec(Map<Long, Long> mem, String mask, long val, int i, long addr) {
    if (i == 36) {
      mem.put(addr, val);
    } else {
      char c = mask.charAt(35 - i);
      long addr0 = addr & (MASK36 ^ (1L << i));
      long addr1 = addr | (1L << i);
      int i2 = i + 1;
      if (c == 'X') {
        rec(mem, mask, val, i2, addr0);
        rec(mem, mask, val, i2, addr1);
      } else if (c == '0') {
        rec(mem, mask, val, i2, addr);
      } else if (c == '1'){
        rec(mem, mask, val, i2, addr1);
      } else {
        throw new RuntimeException();
      }
    }
  }
}
