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
      String[] s1 = s.split(" ");
      String[] split = s1[2].split("=");
      return Pair.of(split[0], Integer.parseInt(split[1]));
    }).collect(Collectors.toList());
  }

  public long solvePart1() {
    Set<Vec2> res = fold(points, instructions.subList(0, 1));
    return res.size();
  }

  public List<String> solvePart2() {
    Set<Vec2> solution = fold(points, instructions);
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

  private Set<Vec2> fold(Set<Vec2> cur, List<Pair<String, Integer>> instr) {
    Set<Vec2> next = new HashSet<>();
    for (Pair<String, Integer> pair : instr) {
      for (Vec2 vec2 : cur) {
        String orient = pair.a();
        int pos = pair.b();
        if (orient.equals("x") && pos < vec2.getX()) {
          vec2 = Vec2.of(2 * pos - vec2.getX(), vec2.getY());
        } else if (orient.equals("y") && pos < vec2.getY()) {
          vec2 = Vec2.of(vec2.getX(), 2 * pos - vec2.getY());

        }
        next.add(vec2);
      }
      cur = next;
      next = new HashSet<>();
    }
    return cur;
  }


}
