package aoc2022;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day7 implements Day {

  private final List<String> input;

  public Day7(String name) {
    input = Util.readResource(name);
  }

  @Override
  public long solvePart1() {
    return parse().findTotal();
  }

  @Override
  public long solvePart2() {
    return parse().findMin(parse().size - 40000000);
  }

  private Node parse() {
    final List<List<String>> lists = input.stream().collect(Util.splitBefore(s -> s.startsWith("$")));

    final Iterator<List<String>> it = lists.iterator();
    it.next(); // skip first cd /
    return recurse(it);
  }

  private Node recurse(Iterator<List<String>> iterator) {
    List<Node> children = new ArrayList<>();
    long size = 0;
    while (iterator.hasNext()) {
      final List<String> next = iterator.next();
      final Iterator<String> nextIter = next.iterator();
      final String cmd = nextIter.next();
      if (cmd.equals("$ ls")) {
        while (nextIter.hasNext()) {
          String s = nextIter.next();
          if (!s.startsWith("dir ")) {
            size += Long.parseLong(s.split(" ")[0]);
          }
        }
      } else if (cmd.equals("$ cd ..")) {
        break;
      } else if (cmd.startsWith("$ cd ")) {
        children.add(recurse(iterator));
      } else {
        throw new RuntimeException(cmd);
      }
    }
    return new Node(size, children);
  }

  static class Node {
    private final long size;
    private final List<Node> children;

    public Node(long size, List<Node> children) {
      this.size = size + children.stream().mapToLong(c -> c.size).sum();
      this.children = children;
    }

    public long findTotal() {
      long sum = 0;
      if (size <= 100000) {
        sum += size;
      }
      return sum + children.stream().mapToLong(Node::findTotal).sum();
    }

    public long findMin(long min) {
      long best = children.stream().mapToLong(c -> c.findMin(min)).min().orElse(Long.MAX_VALUE);
      if (size >= min) {
        return Math.min(best, size);
      }
      return best;
    }
  }
}
