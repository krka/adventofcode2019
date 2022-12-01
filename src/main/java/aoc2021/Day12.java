package aoc2021;

import util.Day;
import util.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Day12 implements Day {

  private final HashMap<String, Set<String>> map;

  public Day12(String name) {
    map = new HashMap<>();
    Util.readResource(name).stream()
            .filter(s -> !s.isEmpty())
            .map(s -> s.split("-"))
            .forEach(strings -> {
              String a = strings[0];
              String b = strings[1];
              map.computeIfAbsent(a, s -> new HashSet<>()).add(b);
              map.computeIfAbsent(b, s -> new HashSet<>()).add(a);
            });
  }

  public long solvePart1() {
    return count("start", new HashSet<>(), false);
  }

  public long solvePart2() {
    return count("start", new HashSet<>(), true);
  }

  private long count(String node, Set<String> visited, boolean allowRevisit) {
    if (node.equals("end")) {
      return 1;
    }
    long sum = 0;
    for (String neighbour : map.getOrDefault(node, Set.of())) {
      if (neighbour.equals("start")) {
        continue;
      }
      boolean isLower = Character.isLowerCase(neighbour.charAt(0));
      if (isLower) {
        boolean v = visited.contains(neighbour);
        if (!v) {
          visited.add(neighbour);
          sum += count(neighbour, visited, allowRevisit);
          visited.remove(neighbour);
        } else if (allowRevisit) {
          sum += count(neighbour, visited, false);
        }
      } else {
        sum += count(neighbour, visited, allowRevisit);
      }
    }
    return sum;
  }

}
