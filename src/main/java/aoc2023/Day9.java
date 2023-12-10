package aoc2023;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9 implements Day {

  private final List<List<String>> lines;
  private final List<List<Integer>> sequences;

  public Day9(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
    sequences = lines.get(0).stream().map(s -> Stream.of(s.split(" ")).map(Integer::parseInt).collect(Collectors.toList())).collect(Collectors.toList());
  }

  @Override
  public long solvePart1() {
    return sequences.stream().mapToLong(this::rec).sum();
  }

  @Override
  public long solvePart2() {
    return sequences.stream().mapToLong(this::rec2).sum();
  }

  private long rec(List<Integer> seq) {
    if (seq.stream().allMatch(integer -> integer == 0)) {
      return 0;
    }
    return (long) seq.get(seq.size() - 1) + rec(diff(seq));
  }

  private long rec2(List<Integer> seq) {
    if (seq.stream().allMatch(integer -> integer == 0)) {
      return 0;
    }
    return (long) seq.get(0) - rec2(diff(seq));
  }

  private List<Integer> diff(List<Integer> seq) {
    final ArrayList<Integer> res = new ArrayList<>();
    for (int i = 1; i < seq.size(); i++) {
      res.add(seq.get(i) - seq.get(i - 1));
    }
    return res;
  }


}
