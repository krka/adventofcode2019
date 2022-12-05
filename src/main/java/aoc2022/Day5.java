package aoc2022;

import util.Day;
import util.DayS;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day5 implements DayS {

  private final List<List<String>> input;
  private final List<String> moves;

  public Day5(String name) {
    input = Util.readResource(name).stream().collect(Util.splitBy(s -> s.isEmpty()));
    moves = input.get(1);
  }

  @Override
  public String solvePart1() {
    List<Stack> stacks = initStacks();
    for (String move : moves) {
      final String[] parts = move.split(" ");
      final int num = Integer.parseInt(parts[1]);
      final int from = Integer.parseInt(parts[3]) - 1;
      final int to = Integer.parseInt(parts[5]) - 1;
      final Stack<Character> fromStack = stacks.get(from);
      final Stack<Character> toStack = stacks.get(to);
      for (int i = 0; i < num; i++) {
        toStack.push(fromStack.pop());
      }
    }
    return toStr(stacks);
  }

  private String toStr(List<Stack> stacks) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 9; i++) {
      sb.append(stacks.get(i).peek());
    }
    return sb.toString();
  }

  private List<Stack> initStacks() {
    final List<String> initial = input.get(0);
    List<Stack> stacks = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      stacks.add(new Stack<>());
    }
    for (int h = 7; h >= 0; h--) {
      final String s = initial.get(h);
      for (int i = 0; i < 9; i++) {
        final char c = s.charAt(i * 4 + 1);
        if (c != ' ') {
          stacks.get(i).push(c);
        }
      }
    }
    return stacks;
  }

  @Override
  public String solvePart2() {
    final List<Stack> stacks = initStacks();
    final Stack<Character> tmp = new Stack<>();
    for (String move : moves) {
      final String[] parts = move.split(" ");
      final int num = Integer.parseInt(parts[1]);
      final int from = Integer.parseInt(parts[3]) - 1;
      final int to = Integer.parseInt(parts[5]) - 1;
      final Stack<Character> fromStack = stacks.get(from);
      final Stack<Character> toStack = stacks.get(to);
      for (int i = 0; i < num; i++) {
        tmp.push(fromStack.pop());
      }
      for (int i = 0; i < num; i++) {
        toStack.push(tmp.pop());
      }
    }
    return toStr(stacks);
  }

}
