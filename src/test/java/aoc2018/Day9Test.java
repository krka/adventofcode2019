package aoc2018;

import org.junit.Test;
import util.Util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import static org.junit.Assert.assertEquals;

public class Day9Test {

  public static final String DAY = "9";
  public static final String YEAR = "2018";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  private static final Pattern PATTERN = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points");
  @Test
  public void testPart1() {
    assertEquals(386018, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(3085518618L, solvePart2(MAIN_INPUT));
  }

  private long solvePart1(String name) {
    return solve(name, 1);
  }

  private long solvePart2(String name) {
    return solve(name, 100);
  }

  private long solve(String name, int factor) {
    List<String> input = Util.readResource(name);

    Matcher matcher = PATTERN.matcher(input.get(0));
    if (!matcher.matches()) {
      throw new RuntimeException();
    }
    int n = Integer.parseInt(matcher.group(1));
    int m = Integer.parseInt(matcher.group(2));
    int numMarbles = m * factor;

    long[] scores = new long[n];

    Node node = new Node(0);
    for (int marble = 1; marble <= numMarbles; marble++) {
      int player = (marble - 1) % n;
      if (marble % 23 == 0) {
        for (int i = 0; i < 7; i++) {
          node = node.prev;
        }
        int removed = node.value;
        node = node.unlinkAndNext();
        scores[player] += marble + removed;
      } else {
        node = node.next;
        node = node.add(marble);
      }
    }
    return LongStream.of(scores).max().getAsLong();
  }


  private static class Node {
    Node prev;
    Node next;
    int value;

    public Node(Node prev, Node next, int value) {
      this.prev = prev;
      this.next = next;
      this.value = value;
    }

    public Node(int value) {
      this(null, null, value);
      prev = this;
      next = this;
    }

    public Node unlinkAndNext() {
      prev.next = next;
      next.prev = prev;
      return next;
    }

    public Node add(int value) {
      Node newNode = new Node(this, next, value);
      next.prev = newNode;
      next = newNode;
      return newNode;
    }
  }
}
