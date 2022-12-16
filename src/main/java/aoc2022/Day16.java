package aoc2022;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day16 implements Day {
  private final List<String> input;

  private final Map<String, Integer> flow = new HashMap<>();
  private final Map<String, List<String>> tunnels = new HashMap<>();

  private final List<String> nonZero = new ArrayList<>();
  private final Map<String, Map<String, Integer>> movement;

  public Day16(String name) {
    input = Util.readResource(name);
    // Valve FI has flow rate=3; tunnels lead to valves DA, AY, ZO, MP, XP
    for (String s : input) {
      if (s.isEmpty()) {
        continue;
      }
      final String[] parts = s.split("[ =,;]+");
      final String name2 = parts[1];
      final int flow = Integer.parseInt(parts[5]);
      if (flow != 0) {
        nonZero.add(name2);
      }
      this.flow.put(name2, flow);
      final ArrayList<String> t = new ArrayList<>();
      tunnels.put(name2, t);
      for (int i = 10; i < parts.length; i++) {
        t.add(parts[i]);
      }
    }
    movement = new HashMap<>();
    for (String s1 : flow.keySet()) {
      final HashMap<String, Integer> moves = new HashMap<>();
      for (String s2 : tunnels.get(s1)) {
        moves.put(s2, 1);
      }
      movement.put(s1, moves);
    }
    final Map<String, Integer> toAdd = new HashMap<>();
    while (true) {
      System.out.println(movement.values().stream().mapToLong(Map::size).sum());
      boolean doBreak = true;
      for (String s1 : flow.keySet()) {
        final Map<String, Integer> move1 = movement.get(s1);
        toAdd.clear();
        move1.forEach((s2, cost1) -> {
          final Map<String, Integer> move2 = movement.get(s2);
          move2.forEach((s3, cost2) -> {
            int totalCost = cost1 + cost2;
            if (!move1.containsKey(s3)) {
              toAdd.put(s3, totalCost);
            } else {
              if (totalCost < move1.get(s3)) {
                toAdd.put(s3, totalCost);
              }
            }
          });
        });
        if (!toAdd.isEmpty()) {
          doBreak = false;
        }
        move1.putAll(toAdd);
      }
      if (doBreak) {
        break;
      }
    }
  }


  @Override
  public long solvePart1() {
    Set<String> visited = new HashSet<>();
    return solve("AA", visited, 30);
  }

  private long solve(String s1, Set<String> visited, int left) {
    long best = 0;
    for (String s2 : nonZero) {
      if (!visited.contains(s2)) {
        visited.add(s2);
        int moveThere = left - movement.get(s1).get(s2) - 1;
        if (moveThere >= 0) {
          final int flow = this.flow.get(s2);
          int addedFlow = moveThere * flow;
          best = Math.max(best, addedFlow + solve(s2, visited, moveThere));
        }
        visited.remove(s2);
      }
    }
    return best;
  }

  @Override
  public long solvePart2() {
    Set<String> visited = new HashSet<>();
    return solve2("AA", "AA", visited, 26, 26);
  }

  private long solve2(String s1, String e1, Set<String> visited, int left, int eleft) {
    long best = 0;
    for (String s2 : nonZero) {
      if (!visited.contains(s2)) {
        visited.add(s2);

        // Move regular
        int moveThere = left - movement.get(s1).get(s2) - 1;

        // move elephant
        int moveThereE = eleft - movement.get(e1).get(s2) - 1;

        final int flow = this.flow.get(s2);

        if (moveThere > moveThereE) {
          if (moveThere >= 0) {
            int addedFlow = moveThere * flow;
            best = Math.max(best, addedFlow + solve2(s2, e1, visited, moveThere, eleft));
          }
        } else {
          if (moveThereE >= 0) {
            int addedFlow = moveThereE * flow;
            best = Math.max(best, addedFlow + solve2(s1, s2, visited, left, moveThereE));
          }
        }


        visited.remove(s2);
      }
    }
    return best;
  }

}



