package aoc2020;

import org.junit.Test;
import util.Pair;
import util.Util;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Day22Test {

  public static final String DAY = "22";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(33694, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(306, solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(31835, solvePart2(MAIN_INPUT));
  }

  @Test
  public void testPart2Sample() {
    assertEquals(291, solvePart2(SAMPLE_INPUT));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);
    List<List<String>> players = input.stream().collect(Util.splitBy(String::isEmpty));

    ArrayDeque<Integer> deck1 = toDeque(players.get(0));
    ArrayDeque<Integer> deck2 = toDeque(players.get(1));

    while (true) {
      if (deck1.isEmpty()) {
        return getScore(deck2);
      }
      if (deck2.isEmpty()) {
        return getScore(deck1);
      }
      int c1 = deck1.pollFirst();
      int c2 = deck2.pollFirst();
      if (c1 > c2) {
        deck1.offerLast(c1);
        deck1.offerLast(c2);
      } else {
        deck2.offerLast(c2);
        deck2.offerLast(c1);
      }
    }
  }

  private ArrayDeque<Integer> toDeque(List<String> list) {
    return list.stream().skip(1).map(Integer::parseInt).collect(Collectors.toCollection(ArrayDeque::new));
  }


  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);
    List<List<String>> players = input.stream().collect(Util.splitBy(String::isEmpty));

    ArrayDeque<Integer> deck1 = toDeque(players.get(0));
    ArrayDeque<Integer> deck2 = toDeque(players.get(1));

    return getScore(rec(deck1, deck2));
  }

  private ArrayDeque<Integer> rec(ArrayDeque<Integer> deck1, ArrayDeque<Integer> deck2) {
    Set<Pair<List<Integer>, List<Integer>>> visited = new HashSet<>();
    while (true) {
      if (deck1.isEmpty()) {
        return deck2;
      }
      if (deck2.isEmpty()) {
        return deck1;
      }
      if (!visited.add(Pair.of(List.copyOf(deck1), List.copyOf(deck2)))) {
        return deck1;
      }
      int c1 = deck1.pollFirst();
      int c2 = deck2.pollFirst();

      boolean p1winner;
      if (deck1.size() >= c1 && deck2.size() >= c2) {
        ArrayDeque<Integer> nd1 = copyLimit(deck1, c1);
        ArrayDeque<Integer> nd2 = copyLimit(deck2, c2);
        p1winner = rec(nd1, nd2) == nd1;
      } else {
        p1winner = c1 > c2;
      }
      if (p1winner) {
        deck1.offerLast(c1);
        deck1.offerLast(c2);
      } else {
        deck2.offerLast(c2);
        deck2.offerLast(c1);
      }
    }
  }

  private ArrayDeque<Integer> copyLimit(ArrayDeque<Integer> deck, int limit) {
    return deck.stream().limit(limit).collect(Collectors.toCollection(ArrayDeque::new));
  }

  private long getScore(ArrayDeque<Integer> winner) {
    int mult = winner.size();
    int score = 0;
    while (!winner.isEmpty()) {
      score += winner.pollFirst() * mult;
      mult--;
    }
    return score;
  }


}
