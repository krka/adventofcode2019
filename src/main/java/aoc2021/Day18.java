package aoc2021;

import util.Day;
import util.Util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day18 implements Day {

  private static final Number[] NUMBERS = new Number[1000];
  private static final Node ZERO = Number.of(0);

  private final List<Node> input;

  public Day18(List<String> input) {
    this.input = input.stream()
            .map(s -> new Parser(s).parse())
            .collect(Collectors.toList());
  }

  public static Day18 fromResource(String name) {
    return new Day18(Util.readResource(name)
            .stream()
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList()));
  }

  public long solvePart1() {
    return input.stream()
            .reduce(Day18::add)
            .get().magnitude();
  }

  public long solvePart2() {
    return input.stream()
            .map(left -> input.stream()
                    .filter(right -> right != left)
                    .mapToLong(right -> add(left, right).magnitude()))
            .flatMapToLong(Function.identity())
            .max().getAsLong();
  }

  private static Node add(Node a, Node b) {
    return reduce(new Pair(a, b));
  }

  private static Node reduce(Node node) {
    Node next = node.explode(4).node.split();
    if (next == node) {
      return next;
    }
    return reduce(next);
  }

  static class ExplodeResult {
    final Node node;
    final int addLeft;
    final int addRight;

    ExplodeResult(Node node, int addLeft, int addRight) {
      this.node = node;
      this.addLeft = addLeft;
      this.addRight = addRight;
    }
  }

  interface Node {
    ExplodeResult explode(int depth);
    Node split();
    int magnitude();
    Node addLeft(int value);
    Node addRight(int value);
  }

  static class Number implements Node {
    final int value;
    final ExplodeResult explodeResult = new ExplodeResult(this, 0, 0);
    final Node split;

    Number(int value) {
      this.value = value;
      if (value >= 10) {
        final int newLeft = value / 2;
        split = new Pair(Number.of(newLeft), Number.of(value - newLeft));
      } else {
        split = this;
      }
    }

    public static Node of(int value) {
      Number obj = NUMBERS[value];
      if (obj == null) {
        obj = new Number(value);
        NUMBERS[value] = obj;
      }
      return obj;
    }

    @Override
    public ExplodeResult explode(int depth) {
      return explodeResult;
    }

    @Override
    public Node split() {
      return split;
    }

    @Override
    public int magnitude() {
      return value;
    }

    @Override
    public Node addLeft(int value) {
      return Number.of(this.value + value);
    }

    @Override
    public Node addRight(int value) {
      return Number.of(this.value + value);
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  static class Pair implements Node {
    final Node left;
    final Node right;

    Pair(Node left, Node right) {
      this.left = left;
      this.right = right;
    }

    @Override
    public int magnitude() {
      return 3 * left.magnitude() + 2 * right.magnitude();
    }

    @Override
    public String toString() {
      return "[" + left + "," + right + "]";
    }

    @Override
    public Node addLeft(int value) {
      return setLeft(left.addLeft(value));
    }

    @Override
    public Node addRight(int value) {
      return new Pair(left, right.addRight(value));
    }

    Pair setLeft(Node newLeft) {
      if (newLeft == left) {
        return this;
      }
      return new Pair(newLeft, right);
    }

    Pair setRight(Node newRight) {
      if (newRight == right) {
        return this;
      }
      return new Pair(left, newRight);
    }

    @Override
    public ExplodeResult explode(int depth) {
      if (depth <= 0 && left instanceof Number && right instanceof Number) {
        return new ExplodeResult(ZERO, ((Number) left).value, ((Number) right).value);
      }

      final ExplodeResult leftExp = left.explode(depth - 1);
      Node right = this.right;
      if (leftExp.addRight != 0) {
        right = right.addLeft(leftExp.addRight);
      }
      Node left = leftExp.node;

      final ExplodeResult rightExp = right.explode(depth - 1);
      if (rightExp.addLeft != 0) {
        left = left.addRight(rightExp.addLeft);
      }
      right = rightExp.node;

      return new ExplodeResult(setLeft(left).setRight(right), leftExp.addLeft, rightExp.addRight);
    }

    @Override
    public Node split() {
      Node newLeft = left.split();
      if (newLeft != left) {
        return new Pair(newLeft, right);
      }
      return setRight(right.split());
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

    public Node parse() {
      char first = consume();
      if (first == '[') {
        Node left = parse();
        expect(',');
        Node right = parse();
        expect(']');
        return new Pair(left, right);
      } else {
        return Number.of(first - '0');
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
