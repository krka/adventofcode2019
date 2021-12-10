package aoc2021;

import util.Util;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day10 {

  private static final Map<Character, Character> OPEN = Map.of(
          '(', ')',
          '[', ']',
          '{', '}',
          '<', '>'
  );

  private static final Map<Character, Integer> CLOSE = Map.of(
          ')', 3,
          ']', 57,
          '}', 1197,
          '>', 25137
  );

  private static final Map<Character, Integer> SCORE = Map.of(
          ')', 1,
          ']', 2,
          '}', 3,
          '>', 4
  );

  private final List<String> input;

  public Day10(String name) {
    this.input = Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
  }

  public long solvePart1() {
    return input.stream().mapToLong(Day10::score).sum();
  }


  private static long score(String s) {
    Stack<Character> stack = new Stack<>();
    for (char c : s.toCharArray()) {
      if (OPEN.containsKey(c)) {
        stack.push(OPEN.get(c));
      } else {
        if (c != stack.pop()) {
          return CLOSE.get(c);
        }
      }
    }
    return 0;
  }

  public long solvePart2() {
    List<Long> collect = input.stream()
            .mapToLong(Day10::score2)
            .filter(c -> c != 0)
            .sorted().boxed()
            .collect(Collectors.toList());
    int size = collect.size();
    return collect.get(size/2);
  }

  private static long score2(String s) {
    Stack<Character> stack = new Stack<>();
    for (char c : s.toCharArray()) {
      if (OPEN.containsKey(c)) {
        stack.push(OPEN.get(c));
      } else {
        if (c != stack.pop()) {
          return 0;
        }
      }
    }
    long sum = 0;
    while (!stack.isEmpty()) {
      sum = sum * 5 + SCORE.get(stack.pop());
    }
    return sum;
  }

}
