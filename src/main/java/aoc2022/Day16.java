package aoc2022;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day16 implements Day {
  private final List<String> input;

  private final Map<String, Integer> flow = new HashMap<>();
  private final Map<String, List<String>> tunnels = new HashMap<>();

  private final List<String> nonZero = new ArrayList<>();
  private final Map<String, Map<String, Integer>> movement;
  private final int[] startMovement;
  private final int[][] movementPrimitive;
  private final int[] flowPrimitive;

  public Day16(String name) {
    input = Util.readResource(name);
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
    flowPrimitive = new int[nonZero.size()];
    movementPrimitive = new int[nonZero.size()][nonZero.size()];
    startMovement = new int[nonZero.size()];
    final Map<String, Integer> startMovementCosts = movement.entrySet().stream().filter(e -> e.getKey().equals("AA"))
            .map(Map.Entry::getValue)
            .flatMap(stringIntegerMap -> stringIntegerMap.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    for (int i = 0; i < flowPrimitive.length; i++) {
      final String s1 = nonZero.get(i);
      startMovement[i] = startMovementCosts.get(s1);
      flowPrimitive[i] = flow.get(s1);
      for (int j = 0; j < flowPrimitive.length; j++) {
        final String s2 = nonZero.get(j);
        movementPrimitive[i][j] = movement.get(s1).get(s2);
      }
    }
  }


  @Override
  public long solvePart1() {
    final int nodes = (1 << flowPrimitive.length) - 1;
    return solve(-1, 0, 30, nodes, flowPrimitive.length);
  }

  private long solve(int from, int visited, int left, int nodes, int length) {
    final int[] ints = from < 0 ? startMovement : movementPrimitive[from];
    long best = 0;
    for (int to = 0; to < length; to++) {
      final int shift = 1 << to;
      if (0 == (nodes & shift)) {
        continue;
      }
      if (0 == (visited & shift)) {
        visited ^= shift;

        int moveThere = left - ints[to] - 1;
        if (moveThere >= 0) {
          final int flow = this.flowPrimitive[to];
          int addedFlow = moveThere * flow;
          best = Math.max(best, addedFlow + solve(to, visited, moveThere, nodes, length));
        }

        visited ^= shift;
      }
    }
    return best;
  }

  @Override
  public long solvePart2() {
    final int length = flowPrimitive.length;
    int max = 1 << length;
    int maxBitmask = max - 1;
    long best = 0;
    for (int left = 0; left < max; left++) {
      int right = ~left & maxBitmask;

      final long leftBest = solve(-1, 0, 26, left, length);
      final long rightBest = solve(-1, 0, 26, right, length);
      best = Math.max(best, leftBest + rightBest);
    }
    return best;
  }

}



