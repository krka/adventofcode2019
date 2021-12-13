package aoc2021;

import util.Pair;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13 {

  private final Set<Vec2> points;
  private final List<Pair<String, Integer>> instructions;

  public Day13(String name) {
    List<List<String>> lists = Util.readResource(name).stream()
            .collect(Util.splitBy(String::isEmpty));
    points = lists.get(0).stream().map(Vec2::parse).collect(Collectors.toSet());
    instructions = lists.get(1).stream().map(s -> {
      String[] split = s.split("[ =]");
      return Pair.of(split[2], Integer.parseInt(split[3]));
    }).collect(Collectors.toList());
  }

  public long solvePart1() {
    return fold(instructions.subList(0, 1)).size();
  }

  public List<String> solvePart2() {
    Set<Vec2> solution = fold(instructions);
    int maxCol = (int) solution.stream().mapToLong(Vec2::getX).max().getAsLong();
    int maxRow = (int) solution.stream().mapToLong(Vec2::getY).max().getAsLong();
    List<String> res = new ArrayList<>();
    for (int row = 0; row <= maxRow; row++) {
      StringBuilder sb = new StringBuilder();
      for (int col = 0; col <= maxCol; col++) {
        sb.append(solution.contains(Vec2.of(col, row)) ? "#" : " ");
      }
      res.add(sb.toString());
    }
    return res;
  }

  private Set<Vec2> fold(List<Pair<String, Integer>> instructions) {
    return points.stream().map(p -> {
      long x = p.getX(), y = p.getY();
      for (var pair : instructions) {
        switch (pair.a()) {
          case "x": x = Math.min(x, 2 * pair.b() - x); break;
          case "y": y = Math.min(y, 2 * pair.b() - y); break;
        }
      }
      return Vec2.of(x, y);
    }).collect(Collectors.toSet());
  }


}
