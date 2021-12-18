package aoc2021;

import util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class Day18 implements Day {

  public static final Number[] NUMBERS = new Number[1000];
  public static final Node ZERO = Number.of(0);

  private final List<Node> input;

  public Day18(List<String> input) {
    this.input = input.stream().map(Day18::parse).collect(Collectors.toList());
  }

  public static Day18 fromResource(String name) {
    return new Day18(Util.readResource(name).stream().filter(s -> !s.isEmpty())
            .collect(Collectors.toList()));
  }

  public long solvePart1() {
    return input.stream()
            .reduce(Day18::add)
            .get().magnitude();
  }

  public long solvePart2() {
    int best = 0;
    for (Node left : input) {
      for (Node right : input) {
        if (left != right) {
          best = Math.max(best, add(left, right).magnitude());
        }
      }
    }
    return best;
  }

  private static Node add(Node a, Node b) {
    return reduce(new Pair(a, b));
  }

  private static Node reduce(Node current) {
    while (true) {
      Node next = current.explode(0).node.split();
      if (next == current) {
        return next;
      }
      current = next;
    }
  }

  static class ExplodeResult {
    final Node node;
    final int addLeft;
    final int addRight;

    public ExplodeResult(Node node, int addLeft, int addRight) {
      this.node = node;
      this.addLeft = addLeft;
      this.addRight = addRight;
    }
  }

  static Node parse(String s) {
    return new Parser(s).parse();
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
    Node split;

    Number(int value) {
      this.value = value;
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
      if (split == null) {
        split = calcSplit();
      }
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

    private Node calcSplit() {
      if (value >= 10) {
        int newLeft = value / 2;
        int newRight = value - newLeft;
        return new Pair(Number.of(newLeft), Number.of(newRight));
      }
      return this;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  static class Pair implements Node {
    final Node left;
    final Node right;

    public Pair(Node left, Node right) {
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
      if (value == 0) {
        return this;
      }
      return setLeft(left.addLeft(value));
    }

    @Override
    public Node addRight(int value) {
      if (value == 0) {
        return this;
      }
      return setRight(right.addRight(value));
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
      if (depth >= 4) {
        if (left instanceof Number && right instanceof Number) {
          return new ExplodeResult(ZERO, ((Number) left).value, ((Number) right).value);
        }
      }
      Node newRight = right;
      Node newLeft = left;

      ExplodeResult leftExp = newLeft.explode(depth + 1);
      newRight = newRight.addLeft(leftExp.addRight);
      newLeft = leftExp.node;

      ExplodeResult rightExp = newRight.explode(depth + 1);
      newLeft = newLeft.addRight(rightExp.addLeft);
      newRight = rightExp.node;

      return new ExplodeResult(setLeft(newLeft).setRight(newRight), leftExp.addLeft, rightExp.addRight);
    }

    @Override
    public Node split() {
      Node newLeft = left.split();
      if (newLeft != left) {
        return setLeft(newLeft);
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
