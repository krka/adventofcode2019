package aoc2023;

import util.Day;
import util.Interval;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 implements Day {

  private final List<List<String>> lines;
  private final List<Long> seeds;
  private final List<List<Mapping>> stages;


  public Day5(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    final String[] s = lines.get(0).get(0).split(" ");
    seeds = new ArrayList<>();
    for (int i = 1; i < s.length; i++) {
      seeds.add(Long.parseLong(s[i]));
    }

    stages = new ArrayList<>();
    for (int i = 1; i < lines.size(); i++) {
      stages.add(toMappings(lines.get(i)));
    }
  }

  private static List<Mapping> toMappings(List<String> lines) {
    final ArrayList<Mapping> res = new ArrayList<>();
    for (int i = 1; i < lines.size(); i++) {
      final String[] s = lines.get(i).split(" ");
      long dest = Long.parseLong(s[0]);
      long source = Long.parseLong(s[1]);
      long len = Long.parseLong(s[2]);
      res.add(new Mapping(source, dest, len));
    }
    return res;
  }

  @Override
  public long solvePart1() {
    return seeds.stream().mapToLong(seed -> {
      long current = seed;
      for (List<Mapping> map : stages) {
        current = remap(current, map);
      }
      return current;
    }).min().getAsLong();
  }

  @Override
  public long solvePart2() {
    List<Interval> intervals = fromSeeds();
    for (List<Mapping> mappings : stages) {
      List<Long> points = allPoints(mappings);
      intervals = Interval.split(intervals, points)
              .stream()
              .map(interval -> remap(interval, mappings))
              .collect(Collectors.toList());
    }
    return intervals.stream().mapToLong(Interval::min).min().getAsLong();
  }

  private List<Interval> fromSeeds() {
    List<Interval> intervals = new ArrayList<>();
    for (int i = 0; i < seeds.size(); i += 2) {
      final long a = seeds.get(i);
      final long b = a + seeds.get(i + 1);
      intervals.add(Interval.of(a, b));
    }
    return intervals;
  }

  private List<Long> allPoints(List<Mapping> mappings) {
    return mappings.stream().flatMap(x -> Stream.of(x.interval.min(), x.interval.max()))
            .sorted()
            .distinct()
            .collect(Collectors.toList());
  }

  private Interval remap(Interval interval, List<Mapping> mappings) {
    for (Mapping mapping : mappings) {
      if (interval.inside(mapping.interval)) {
        return interval.shift(mapping.delta);
      }
    }
    return interval;
  }

  private long remap(long point, List<Mapping> mappings) {
    for (Mapping mapping : mappings) {
      if (mapping.interval.contains(point)) {
        return point + mapping.delta;
      }
    }
    return point;
  }

  private static class Mapping {
    final Interval interval;
    final long delta;

    public Mapping(long source, long dest, long len) {
      this.interval = Interval.of(source, source + len);
      this.delta = dest - source;
    }
  }
}
