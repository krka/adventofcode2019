package aoc2021;

import util.Matrix3;
import util.Util;
import util.Vector3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day19 implements Day {

  private final List<Set<Vector3>> input;

  private Map<Vector3, Set<Vector3>> answer;

  public Day19(List<String> input) {
    this.input = input.stream()
            .collect(Util.splitBy(String::isEmpty))
            .stream()
            .map(strings -> strings.stream()
                    .filter(s -> !s.isEmpty() && !s.startsWith("--"))
                    .map(Vector3::parse)
                    .collect(Collectors.toSet()))
            .collect(Collectors.toList());
  }

  public static Day19 fromResource(String name) {
    return new Day19(Util.readResource(name));
  }

  public long solvePart1() {
    return solve().values().stream()
            .flatMap(Collection::stream)
            .distinct()
            .count();
  }

  public long solvePart2() {
    Set<Vector3> scanners = solve().keySet();
    return scanners.stream().flatMapToLong(s1 -> scanners.stream().mapToLong(s2 -> s2.sub(s1).manhattan())).max().getAsLong();
  }

  private synchronized Map<Vector3, Set<Vector3>> solve() {
    if (answer == null) {
      answer = reduce(input);
    }
    return answer;
  }

  private Map<Vector3, Set<Vector3>> reduce(List<Set<Vector3>> in) {
    List<List<Match>> matches = new ArrayList<>();
    for (int i = 0; i < in.size(); i++) {
      matches.add(new ArrayList<>());
    }

    for (int i = 0; i < in.size() - 1; i++) {
      Set<Vector3> left = in.get(i);
      for (Matrix3 rotation : Matrix3.ROTATIONS) {
        Matrix3 invert = Matrix3.INVERT_ROTATIONS.get(rotation);
        Set<Vector3> leftRot = left.stream().map(rotation::mul).collect(Collectors.toSet());
        for (int j = i + 1; j < in.size(); j++) {
          Vector3 offset = checkMatch(leftRot, in.get(j));
          if (offset != null) {
            matches.get(j).add(new Match(i, j, rotation, offset));
            matches.get(i).add(new Match(j, i, invert, invert.mul(offset.negate())));
          }
        }
      }
    }

    Map<Vector3, Set<Vector3>> result = new HashMap<>();
    dfs(in, matches, result, 0, Function.identity());

    if (result.size() != in.size()) {
      throw new RuntimeException("Not a complete graph");
    }

    return result;
  }

  private void dfs(List<Set<Vector3>> input, List<List<Match>> matches, Map<Vector3, Set<Vector3>> result, int index, Function<Vector3, Vector3> transform) {
    Vector3 scanner = transform.apply(Vector3.zero());
    if (!result.containsKey(scanner)) {
      result.put(scanner, input.get(index).stream().map(transform).collect(Collectors.toSet()));
      matches.get(index)
              .forEach(match -> dfs(input, matches, result, match.destIndex, v -> transform.apply(match.rotation.mul(v).add(match.offset))));
    }

  }

  private Vector3 checkMatch(Set<Vector3> dest, Set<Vector3> source) {
    Map<Vector3, AtomicInteger> diffs = new HashMap<>();
    for (Vector3 d : dest) {
      for (Vector3 s : source) {
        Vector3 diff = s.sub(d);
        AtomicInteger counter = diffs.computeIfAbsent(diff, k -> new AtomicInteger());
        if (counter.incrementAndGet() >= 12) {
          return diff;
        }
      }
    }
    return null;
  }

  private static class Match {
    final int destIndex;
    final int sourceIndex;
    final Matrix3 rotation;
    final Vector3 offset;

    public Match(int destIndex, int index2, Matrix3 rotation, Vector3 offset) {
      this.destIndex = destIndex;
      this.sourceIndex = index2;
      this.rotation = rotation;
      this.offset = offset;
    }
  }

}
