package aoc2022;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day11 implements Day {

  private final List<List<String>> input;

  public Day11(String name) {
    input = Util.readResource(name).stream().collect(Util.splitBy(s -> s.isEmpty()));
  }

  @Override
  public long solvePart1() {
    return solve(20, 3);
  }

  @Override
  public long solvePart2() {
    return solve(10000, 1);
  }

  private long solve(int numRounds, int divisor) {
    Monkey[] monkeys = new Monkey[input.size()];
    int i = 0;
    for (List<String> monkey : input) {
      monkeys[i++] = parse(monkey);
    }
    long keepSmall = 1;
    for (Monkey monkey : monkeys) {
      keepSmall = keepSmall * monkey.divisibleBy;
    }
    for (int round = 0; round < numRounds; round++) {
      for (final Monkey monkey : monkeys) {
        for (Long item : monkey.items) {
          long level = item;
          if (monkey.worryOperator == '+') {
            level = level + monkey.worryOperand;
          } else if (monkey.worryOperator == '^') {
            level = level * level;
          } else {
            level = level * monkey.worryOperand;
          }
          level = level / divisor;
          level = level % keepSmall;
          final long remainder = level % monkey.divisibleBy;
          final int target = remainder == 0 ? monkey.ifTrue : monkey.ifFalse;
          monkeys[target].items.add(level);
        }
        monkey.count += monkey.items.size();
        monkey.items.clear();
      }
    }
    return Arrays.stream(monkeys).mapToLong(m -> -m.count).sorted().limit(2).map(x -> -x).reduce(1, (a,b)->a*b);
  }

  static class Monkey {
    final List<Long> items = new ArrayList<>();
    final char worryOperator;
    final long worryOperand;
    final long divisibleBy;
    final int ifTrue;
    final int ifFalse;

    long count = 0;

    public Monkey(char worryOperator, int worryOperand, int divisibleBy, int ifTrue, int ifFalse, List<Long> list) {
      this.worryOperator = worryOperator;
      this.worryOperand = worryOperand;
      this.divisibleBy = divisibleBy;
      this.ifTrue = ifTrue;
      this.ifFalse = ifFalse;
      items.addAll(list);
    }
  }

  static Monkey parse(List<String> lines) {
    final List<Long> list = new ArrayList<>();
    String[] parts = lines.get(1).split("[:, ]+");
    for (int i = 3; i < parts.length; i++) {
      list.add(Long.parseLong(parts[i]));
    }

    parts = lines.get(2).split("[:, ]+");
    char operator = parts[5].charAt(0);
    final String part = parts[6];
    int operand = 2;
    if (part.equals("old")) {
      operator = '^';
    } else {
      operand = Integer.parseInt(part);
    }

    parts = lines.get(3).split("[ ]");
    int divisibleBy = Integer.parseInt(parts[parts.length - 1]);

    parts = lines.get(4).split("[ ]");
    int ifTrue = Integer.parseInt(parts[parts.length - 1]);

    parts = lines.get(5).split("[ ]");
    int ifFalse = Integer.parseInt(parts[parts.length - 1]);
    return new Monkey(operator, operand, divisibleBy, ifTrue, ifFalse, list);
  }
}


