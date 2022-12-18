package aoc2021;

import util.Day;
import util.Matrix3;
import util.Util;
import util.Vec3;

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

  private final List<Set<Vec3>> input;

  private Map<Vec3, Set<Vec3>> answer;

  public Day19(List<String> input) {
    this.input = input.stream()
            .collect(Util.splitBy(String::isEmpty))
            .stream()
            .map(strings -> strings.stream()
                    .filter(s -> !s.isEmpty() && !s.startsWith("--"))
                    .map(Vec3::parse)
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
    Set<Vec3> scanners = solve().keySet();
    return scanners.stream().flatMapToLong(s1 -> scanners.stream().mapToLong(s2 -> s2.sub(s1).manhattan())).max().getAsLong();
  }

  private synchronized Map<Vec3, Set<Vec3>> solve() {
    if (answer == null) {
      answer = reduce(input);
    }
    return answer;
  }

  private Map<Vec3, Set<Vec3>> reduce(List<Set<Vec3>> in) {
    List<List<Match>> matches = new ArrayList<>();
    for (int i = 0; i < in.size(); i++) {
      matches.add(new ArrayList<>());
    }

    for (int i = 0; i < in.size() - 1; i++) {
      Set<Vec3> left = in.get(i);
      for (Matrix3 rotation : Matrix3.ROTATIONS) {
        Matrix3 invert = Matrix3.INVERT_ROTATIONS.get(rotation);
        Set<Vec3> leftRot = left.stream().map(rotation::mul).collect(Collectors.toSet());
        for (int j = i + 1; j < in.size(); j++) {
          Vec3 offset = checkMatch(leftRot, in.get(j));
          if (offset != null) {
            matches.get(j).add(new Match(i, j, rotation, offset));
            matches.get(i).add(new Match(j, i, invert, invert.mul(offset.negate())));
          }
        }
      }
    }

    Map<Vec3, Set<Vec3>> result = new HashMap<>();
    dfs(in, matches, result, 0, Function.identity());

    if (result.size() != in.size()) {
      throw new RuntimeException("Not a complete graph");
    }

    return result;
  }

  private void dfs(List<Set<Vec3>> input, List<List<Match>> matches, Map<Vec3, Set<Vec3>> result, int index, Function<Vec3, Vec3> transform) {
    Vec3 scanner = transform.apply(Vec3.zero());
    if (!result.containsKey(scanner)) {
      result.put(scanner, input.get(index).stream().map(transform).collect(Collectors.toSet()));
      matches.get(index)
              .forEach(match -> dfs(input, matches, result, match.destIndex, v -> transform.apply(match.rotation.mul(v).add(match.offset))));
    }

  }

  private Vec3 checkMatch(Set<Vec3> dest, Set<Vec3> source) {
    Map<Vec3, AtomicInteger> diffs = new HashMap<>();
    for (Vec3 d : dest) {
      for (Vec3 s : source) {
        Vec3 diff = s.sub(d);
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
    final Vec3 offset;

    public Match(int destIndex, int index2, Matrix3 rotation, Vec3 offset) {
      this.destIndex = destIndex;
      this.sourceIndex = index2;
      this.rotation = rotation;
      this.offset = offset;
    }
  }

}
