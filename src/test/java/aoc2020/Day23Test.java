package aoc2020;

import org.junit.Test;
import util.LLNode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day23Test {

  public static final String DAY = "23";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = "247819356";
  public static final String SAMPLE_INPUT = "389125467";

  private List<LLNode<Integer>> lookup;

  @Test
  public void testPart1() {
    assertEquals("76385429", solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals("67384529", solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(12621748849L, solvePart2(MAIN_INPUT));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(149245887792L, solvePart2(SAMPLE_INPUT));
  }

  private String solvePart1(String name) {
    solve(name, 100, 9);
    return stateTostring().substring(1);
  }

  private String stateTostring() {
    LLNode<Integer> first = lookup.get(1);
    LLNode<Integer> current = first;
    StringBuilder sb = new StringBuilder();
    while (true) {
      sb.append((char) (current.value + '0'));
      current = current.next;
      if (current == first) {
        return sb.toString();
      }
    }
  }

  private long solvePart2(String name) {
    solve(name, 10_000_000, 1000000);
    LLNode<Integer> first = lookup.get(1);
    LLNode<Integer> second = first.next;
    LLNode<Integer> third = second.next;
    long v1 = second.value;
    long v2 = third.value;
    return v1 * v2;
  }

  private void solve(String name, int loops, int n) {
    lookup = new ArrayList<>(n + 1);

    // Dummy
    lookup.add(null);

    for (int i = 1; i <= n; i++) {
      lookup.add(LLNode.of(i));
    }

    LLNode<Integer> first = null;
    LLNode<Integer> prev = null;

    char[] fixed = name.toCharArray();
    for (char c : fixed) {
      LLNode<Integer> cur = lookup.get(c - '0');
      if (first == null) {
        first = cur;
      }
      if (prev != null) {
        prev.addAfter(cur);
      }
      prev = cur;
    }
    for (int i = 1 + fixed.length; i <= n; i++) {
      LLNode<Integer> cur = lookup.get(i);
      prev.addAfter(cur);
      prev = cur;
    }

    first.prev = prev;
    prev.next = first;

    LLNode<Integer> current = first;
    for (int loop = 0; loop < loops; loop++) {
      int curval = current.value;
      LLNode<Integer> rem1 = current.removeNext();
      LLNode<Integer> rem2 = current.removeNext();
      LLNode<Integer> rem3 = current.removeNext();

      int dest = curval;
      LLNode<Integer> destNode;
      do {
        dest--;
        if (dest == 0) {
          dest = n;
        }
        destNode = lookup.get(dest);
      } while (!destNode.isAttached());
      destNode.addAfter(rem1);
      rem1.addAfter(rem2);
      rem2.addAfter(rem3);

      current = current.next;
    }
  }
}
