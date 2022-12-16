package aoc2022;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
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
  private final int[] flowPrimitive;
  private final int length;
  private final int[][] costs;
  private final int[][][][] cache;

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
    length = nonZero.size();
    flowPrimitive = new int[length];
    costs = new int[length + 1][length];
    final Map<String, Integer> startMovementCosts = movement.entrySet().stream().filter(e -> e.getKey().equals("AA"))
            .map(Map.Entry::getValue)
            .flatMap(stringIntegerMap -> stringIntegerMap.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    for (int i = 0; i < length; i++) {
      final String s1 = nonZero.get(i);
      costs[length][i] = startMovementCosts.get(s1);
      flowPrimitive[i] = flow.get(s1);
      for (int j = 0; j < length; j++) {
        final String s2 = nonZero.get(j);
        costs[i][j] = movement.get(s1).get(s2);
      }
    }
    int max = 1 << length;
    cache = new int[length + 1][max][31][2];
  }


  @Override
  public long solvePart1() {
    return solve(length, 0, 30, 0);
  }

  @Override
  public long solvePart2() {
    return solve(length, 0, 26, 1);
  }

  private int solve(int from, int visited, int timeLeft, int playersLeft) {
    final int cached = cache[from][visited][timeLeft][playersLeft];
    if (cached != 0) {
      return cached - 1;
    }
    final int[] cost = costs[from];
    int best = 0;
    if (playersLeft > 0) {
      best = Math.max(best, solve(length, visited, 26, playersLeft - 1));
    }
    for (int to = 0; to < length; to++) {
      final int bit = 1 << to;
      if (0 == (visited & bit)) {
        int timeLeftAfter = timeLeft - cost[to] - 1;
        if (timeLeftAfter >= 0) {
          final int flow = flowPrimitive[to];
          int addedFlow = timeLeftAfter * flow;
          final int newVisited = visited | bit;
          best = Math.max(best, addedFlow + solve(to, newVisited, timeLeftAfter, playersLeft));
        }
      }
    }
    cache[from][visited][timeLeft][playersLeft] = best + 1;
    return best;
  }

}



