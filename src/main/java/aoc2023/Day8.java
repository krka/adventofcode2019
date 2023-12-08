package aoc2023;

import util.Day;
import util.Util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day8 implements Day {

  private final List<List<String>> lines;
  private final String sequence;
  private final List<Node> nodes;
  private final Map<String, Node> nodeMap;

  public Day8(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    sequence = lines.get(0).get(0);
    nodes = lines.get(1).stream().map(s -> s.split("[ =, ()]+")).map(Day8::toNode).collect(Collectors.toList());
    nodeMap = new HashMap<>();
    for (Node node : this.nodes) {
      nodeMap.put(node.name, node);
    }
    try (FileOutputStream stream = new FileOutputStream(name.replace("/", "_") + ".dot")) {
      final String s = toGraphViz(nodeMap);
      stream.write(s.getBytes(StandardCharsets.UTF_8));
      // neato -Tpng -x -o day8.png 2023_day8.in.dot
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String toGraphViz(Map<String, Node> nodeMap) {
    StringBuilder sb = new StringBuilder();
    sb.append("digraph {\n");
    nodeMap.forEach((s, node) -> {
      if (s.endsWith("Z")) {
        sb.append(s + " [style=filled,color=\".5 .5 1.0\"]\n");
      }
      if (s.endsWith("A")) {
        sb.append(s + " [style=filled,color=\".5 1.0 .5\"]\n");
      }
      sb.append(s + " -> " + node.left + ", " + node.right + ";\n");
    });
    sb.append("}\n");
    return sb.toString();
  }

  private static Node toNode(String[] strings) {
    return new Node(strings[0].trim(), strings[1].trim(), strings[2].trim());
  }

  @Override
  public long solvePart1() {
    return solve("AAA", "ZZZ");
  }

  @Override
  public long solvePart2() {
    return nodes.stream().map(s -> s.name)
            .filter(s -> s.endsWith("A"))
            .mapToLong(s -> solve(s, "Z"))
            .map(l -> {
              System.out.println(Util.factors(l));
              return l;
            })
            .reduce(1L, Util::lcm);
  }

  private int solve(String current, String end) {
    int steps = 0;
    while (!current.endsWith(end)) {
      final Node node = nodeMap.get(current);
      final int action = steps % sequence.length();
      if (sequence.charAt(action) == 'R') {
        current = node.right;
      } else {
        current = node.left;
      }
      steps++;
    }
    return steps;
  }

  private static class Node {
    final String name;
    final String left;
    final String right;

    public Node(String name, String left, String right) {
      this.name = name;
      this.left = left;
      this.right = right;
    }
  }
}
