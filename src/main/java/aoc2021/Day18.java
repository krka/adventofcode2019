package aoc2021;

import util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class Day18 implements Day {

  private final List<String> input;

  public Day18(List<String> input) {
    this.input = input;
  }

  public static Day18 fromResource(String name) {
    return new Day18(Util.readResource(name).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()));
  }

  public long solvePart1() {
    return input.stream()
            .map(P::parse)
            .reduce((a, b) -> reduce(P.from(a, b)))
            .get().magnitude();
  }

  public long solvePart2() {
    long best = 0;
    for (String s1 : input) {
      for (String s2 : input) {
        if (!s1.equals(s2)) {
          P left = P.parse(s1);
          P right = P.parse(s2);
          best = Math.max(best, reduce(P.from(left, right)).magnitude());
        }
      }
    }
    return best;
  }


  private P reduce(P current) {
    P next = current.explode(0).node;
    if (next != current) {
      return reduce(next);
    }

    next = split(current);
    if (next != current) {
      return reduce(next);
    }

    return next;
  }

  private P split(P current) {
    if (current.number >= 10) {
      int newLeft = current.number / 2;
      int newRight = current.number - newLeft;
      return P.from(P.number(newLeft), P.number(newRight));
    }

    if (current.isLeaf()) {
      return current;
    }

    P left = split(current.left);
    if (left != current.left) {
      return current.setLeft(left);
    }
    return current.setRight(split(current.right));
  }

  static class ExplodeResult {
    final P node;
    final int addLeft;
    final int addRight;

    public ExplodeResult(P node, int addLeft, int addRight) {
      this.node = node;
      this.addLeft = addLeft;
      this.addRight = addRight;
    }
  }

  static class P {
    final int number;
    final P left;
    final P right;

    public P(int number, P left, P right) {
      this.number = number;
      this.left = left;
      this.right = right;
    }

    static P parse(String s) {
      return new Parser(s).parse();
    }

    static P from(P left, P right) {
      return new P(0, left, right);
    }

    static P number(int number) {
      return new P(number, null, null);
    }

    public long magnitude() {
      if (isLeaf()) {
        return number;
      }
      return 3L * left.magnitude() + 2L * right.magnitude();
    }

    private boolean isLeaf() {
      return left == null;
    }

    @Override
    public String toString() {
      if (isLeaf()) {
        return "" + number;
      }
      return "[" + left + "," + right + "]";
    }

    public P addLeft(int value) {
      if (value == 0) {
        return this;
      }
      if (isLeaf()) {
        return P.number(this.number + value);
      }
      return setLeft(left.addLeft(value));
    }

    private P addRight(int value) {
      if (value == 0) {
        return this;
      }
      if (isLeaf()) {
        return P.number(this.number + value);
      }
      return setRight(right.addRight(value));
    }

    P setLeft(P newLeft) {
      if (newLeft == left) {
        return this;
      }
      return P.from(newLeft, right);
    }

    P setRight(P newRight) {
      if (newRight == right) {
        return this;
      }
      return P.from(left, newRight);
    }

    private ExplodeResult explode(int depth) {
      if (isLeaf()) {
        return new ExplodeResult(this, 0, 0);
      }
      if (depth >= 4) {
        if (left.isLeaf() && right.isLeaf()) {
          return new ExplodeResult(number(0), left.number, right.number);
        }
      }
      P newRight = right;
      P newLeft = left;

      ExplodeResult leftExp = newLeft.explode(depth + 1);
      newRight = newRight.addLeft(leftExp.addRight);
      newLeft = leftExp.node;

      ExplodeResult rightExp = newRight.explode(depth + 1);
      newLeft = newLeft.addRight(rightExp.addLeft);
      newRight = rightExp.node;

      return new ExplodeResult(setLeft(newLeft).setRight(newRight), leftExp.addLeft, rightExp.addRight);
    }
  }

  private static class Parser {
    final String s;
    int pos;

    public Parser(String s) {
      this.s = s;
    }

    char consume() {
      return s.charAt(pos++);
    }

    public P parse() {
      char first = consume();
      if (first == '[') {
        P left = parse();
        expect(',');
        P right = parse();
        expect(']');
        return P.from(left, right);
      } else {
        return P.number(first - '0');
      }
    }

    private void expect(char expected) {
      char actual = consume();
      if (actual != expected) {
        throw new RuntimeException("At Pos " + pos + ". Expected " + expected + " but got " + actual);
      }
    }
  }
}
