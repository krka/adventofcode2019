package aoc2020;

import util.Pair;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 {
  private static final Pattern PATTERN = Pattern.compile("(.*) bags contain (.*).");
  private static final Pattern PATTERN2 = Pattern.compile("(\\d+) (.*?) bag[s]?[,]?[\\s]*");

  private final List<String> input;
  private final Map<String, List<Pair<Integer, String>>> sourceToTargets;
  private final Map<String, Set<String>> targetToSources;

  public Day7(String name) {
    input = Util.readResource(name);

    sourceToTargets = new HashMap<>();
    targetToSources = new HashMap<>();
    for (String s : input) {
      Matcher matcher = PATTERN.matcher(s);
      if (matcher.matches()) {
        String source = matcher.group(1);
        String target = matcher.group(2);
        ArrayList<Pair<Integer, String>> objects = new ArrayList<>();
        Matcher matcher2 = PATTERN2.matcher(target);
        while (matcher2.find()) {
          int count = Integer.parseInt(matcher2.group(1));
          String target2 = matcher2.group(2);
          objects.add(Pair.of(count, target2));
          targetToSources.computeIfAbsent(target2, x -> new HashSet<>()).add(source);
        }
        sourceToTargets.put(source, objects);
      } else {
        throw new RuntimeException();
      }
    }

  }

  public long solvePart1() {
    Set<String> visited = new HashSet<>();
    dfs("shiny gold", visited, targetToSources);
    return visited.size() - 1;
  }

  private void dfs(String s, Set<String> visited, Map<String, Set<String>> rev) {
    if (visited.add(s)) {
      Set<String> strings = rev.get(s);
      if (strings != null) {
        for (String string : strings) {
          dfs(string, visited, rev);
        }
      }
    }
  }

  public long solvePart2() {
    return dfs2("shiny gold") - 1;
  }

  private long dfs2(String s) {
    return 1 + sourceToTargets.get(s).stream().mapToLong(p -> p.a() * dfs2(p.b())).sum();
  }

}
