package aoc2021;

import util.Util;

import java.util.Locale;
import java.util.function.LongBinaryOperator;

public class Day16 implements Day {


  private final String data;
  private int pc;

  private int sum;

  public Day16(String input) {
    StringBuilder sb = new StringBuilder();
    for (char c : input.toCharArray()) {
      String s = Integer.toBinaryString(Integer.parseInt("" + c, 16));
      String format = String.format(Locale.ROOT, "%4s", s);
      sb.append(format);
    }
    data = sb.toString().replace(' ', '0');
    pc = 0;
    sum = 0;
  }

  public static Day16 fromResource(String name) {
    return new Day16(Util.readResource(name).get(0));
  }

  public long solvePart1() {
    parsePacket();
    return sum;
  }

  public long solvePart2() {
    return parsePacket();
  }

  private int consumeInt(int n) {
    int res = 0;
    while (--n >= 0) {
      res = res * 2 + (data.charAt(pc++) - '0');
    }
    return res;
  }

  private long parsePacket() {
    int version = consumeInt(3);
    int type = consumeInt(3);
    sum += version;
    switch (type) {
      case 0: return operator(0, (a,b) -> a + b);
      case 1: return operator(1, (a,b) -> a * b);
      case 2: return operator(Long.MAX_VALUE, Math::min);
      case 3: return operator(Long.MIN_VALUE, Math::max);
      case 4: return literal();
      case 5: return operator(Long.MIN_VALUE, (a,b) -> a == Long.MIN_VALUE ? b : a > b ? 1 : 0);
      case 6: return operator(Long.MIN_VALUE, (a,b) -> a == Long.MIN_VALUE ? b : a < b ? 1 : 0);
      case 7: return operator(Long.MIN_VALUE, (a,b) -> a == Long.MIN_VALUE ? b : a == b ? 1 : 0);
      default: throw new RuntimeException();
    }
  }

  private long operator(long init, LongBinaryOperator operator) {
    long cur = init;
    if (consumeInt(1) == 0) {
      int len = consumeInt(15);
      int end = pc + len;
      while (pc < end) {
        cur = operator.applyAsLong(cur, parsePacket());
      }
    } else {
      int num = consumeInt(11);
      for (int i = 0; i < num; i++) {
        cur = operator.applyAsLong(cur, parsePacket());
      }
    }
    return cur;
  }

  private long literal() {
    long res = 0;
    while (true) {
      boolean stop = consumeInt(1) == 0;
      res = res * 16 + consumeInt(4);
      if (stop) {
        return res;
      }
    }
  }

}
