package aoc2023;

import util.Day;
import util.Pair;
import util.Util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7 implements Day {

  private final static List<Character> ORDER = Util.toList("AKQJT98765432");
  private final static Map<Character, Integer> ORDER_MAP = Util.zipWithIndex(ORDER).stream().collect(Collectors.toMap(Pair::a, Pair::b));

  private final static List<Character> JOKER_ORDER = Util.toList("AKQT98765432J");
  private final static Map<Character, Integer> JOKER_ORDER_MAP = Util.zipWithIndex(JOKER_ORDER).stream().collect(Collectors.toMap(Pair::a, Pair::b));

  private final List<List<String>> lines;
  private final List<Hand> cards;

  public Day7(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    cards = lines.get(0).stream().map(this::toHand).collect(Collectors.toList());
  }

  private Hand toHand(String line) {
    final String[] s = line.split(" ");
    return new Hand(s[0], Integer.parseInt(s[1]));
  }

  @Override
  public long solvePart1() {
    final Comparator<Hand> comparator = Util.comparingEnum(Hand::type)
            .thenComparing(Util.comparingList(Hand::getOrder))
            .reversed();
    return sum(cards, comparator);
  }

  @Override
  public long solvePart2() {
    final Comparator<Hand> comparator = Util.comparingEnum(Hand::jokerType)
            .thenComparing(Util.comparingList(Hand::getJokerOrder))
            .reversed();
    return sum(cards, comparator);
  }

  private int sum(List<Hand> hands, Comparator<Hand> comparator) {
    final List<Integer> sortedBets = hands.stream().sorted(comparator).map(Hand::bet).collect(Collectors.toList());
    return Util.zipWithIndex(sortedBets).stream()
            .mapToInt(pair -> pair.a() * (pair.b() + 1))
            .sum();
  }

  private static class Hand {
    final String cards;
    final int bet;
    final Type type;
    final Type jokerType;
    final List<Integer> order;
    final List<Integer> jokerOrder;

    public Hand(String cards, int bet) {
      this.cards = cards;
      this.bet = bet;
      this.type = getType(cards);
      this.jokerType = getJokerType(cards);
      this.order = toOrder(cards, ORDER_MAP);
      this.jokerOrder = toOrder(cards, JOKER_ORDER_MAP);
    }

    private static List<Integer> toOrder(String cards, Map<Character, Integer> orderMap) {
      return Util.toList(cards).stream().map(orderMap::get).collect(Collectors.toList());
    }

    private Type getJokerType(String cards) {
      return Util.toSet(cards).stream().map(c -> getType(cards.replace('J', c))).min(Util.comparingEnum()).get();
    }

    private Type getType(String cards) {
      final List<Integer> pattern = Util.toList(cards).stream()
              .collect(Collectors.groupingBy(Function.identity()))
              .values().stream()
              .map(List::size)
              .sorted()
              .collect(Collectors.toList());
      return Stream.of(Type.values()).filter(t -> t.pattern.equals(pattern)).findAny().get();
    }

    public Type type() {
      return type;
    }

    public Type jokerType() {
      return jokerType;
    }

    public int bet() {
      return bet;
    }

    public List<Integer> getOrder() {
      return order;
    }

    public List<Integer> getJokerOrder() {
      return jokerOrder;
    }

    @Override
    public String toString() {
      return "MyCard{" +
              "hand='" + cards + '\'' +
              ", bet=" + bet +
              ", type=" + type +
              '}';
    }
  }

  enum Type {
    FIVE(5),
    FOUR(1, 4),
    FULL_HOUSE(2, 3),
    THREE(1, 1, 3),
    TWO_PAIR(1, 2, 2),
    PAIR(1, 1, 1, 2),
    HIGH(1,1,1,1,1);

    private final List<Integer> pattern;

    Type(int... pattern) {
      this.pattern = IntStream.of(pattern).boxed().collect(Collectors.toList());
    }
  }
}
