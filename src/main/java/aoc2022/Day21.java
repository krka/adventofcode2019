package aoc2022;

import util.Day;
import util.Util;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Day21 implements Day {

  private final HashMap<String, Expr> map;

  public Day21(String name) {
    map = new HashMap<>();
    Util.readResource(name).stream().filter(s -> !s.isEmpty())
            .collect(Collectors.toList())
    .forEach(s -> {
      final String[] parts = s.split("[: ]+");
      final String key = parts[0];
      final String op1 = parts[1];
      if (parts.length > 2) {
        final String operator = parts[2];
        final String op2 = parts[3];
        map.put(key, new Expr(op1, op2, operator, 0));
      } else {
        final Expr e = new Expr(null, null, null, Long.parseLong(op1));
        map.put(key, e);
      }
    });
  }


  @Override
  public long solvePart1() {
    return eval1("root");
  }

  private long eval1(String node) {
    final Expr expr = map.get(node);
    if (expr.op1 == null) {
      return expr.value;
    }
    final long left = eval1(expr.op1);
    final long right = eval1(expr.op2);
    switch (expr.operator) {
      case "+": return left + right;
      case "-": return left - right;
      case "*": return left * right;
      case "/": return div(left, right);
      default: throw new RuntimeException();
    }
  }

  @Override
  public long solvePart2() {
    final Expr expr = map.get("root");

    final Long left = eval(expr.op1);
    final Long right = eval(expr.op2);

    if (left == null) {
      return set(expr.op1, right);
    }
    if (right == null) {
      return set(expr.op2, left);
    }

    throw new RuntimeException();
  }

  private Long set(String node, long value) {
    if (node.equals("humn")) {
      return value;
    }
    final Expr expr = map.get(node);
    if (expr.op1 == null) {
      throw new RuntimeException();
    }
    final Long left = eval(expr.op1);
    final Long right = eval(expr.op2);

    if (left == null) {
      return set(expr.op1, inverseOpLeft(expr.operator, value, right));
    }

    if (right == null) {
      return set(expr.op2, inverseOpRight(expr.operator, value, left));
    }

    throw new RuntimeException();
  }

  private long inverseOpRight(String op, long target, long left) {
    switch (op) {
      case "+": return target - left;
      case "-": return left - target;
      case "*": return div(target, left);
      case "/": return div(left, target);
      default: throw new RuntimeException();
    }
  }

  private long inverseOpLeft(String op, long target, long right) {
    switch (op) {
      case "+": return target - right;
      case "-": return target + right;
      case "*": return div(target, right);
      case "/": return target * right;
      default: throw new RuntimeException();
    }
  }

  private long div(long a, long b) {
    if (0 != (a % b)) {
      throw new RuntimeException();
    }
    return a / b;
  }

  private Long eval(String node) {
    if (node.equals("humn")) {
      return null;
    }
    final Expr expr = map.get(node);
    if (expr.op1 == null) {
      return expr.value;
    }
    Long v1 = eval(expr.op1);
    Long v2 = eval(expr.op2);
    if (v1 != null && v2 != null) {
      switch (expr.operator) {
        case "+": return v1 + v2;
        case "-": return v1 - v2;
        case "*": return v1 * v2;
        case "/": return div(v1, v2);
        default: throw new RuntimeException();
      }
    }

    return null;
  }

  private static class Expr {
    final String op1;
    final String op2;
    final String operator;
    final long value;

    public Expr(String op1, String op2, String operator, long value) {
      this.op1 = op1;
      this.op2 = op2;
      this.operator = operator;
      this.value = value;
    }
  }
}
