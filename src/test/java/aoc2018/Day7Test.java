package aoc2018;

import org.junit.Test;
import util.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Day7Test {

  public static final String DAY = "7";
  public static final String YEAR = "2018";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  private static final Pattern PATTERN = Pattern.compile("Step (.) must be finished before step (.) can begin.");
  @Test
  public void testPart1() {
    assertEquals("BGJCNLQUYIFMOEZTADKSPVXRHW", solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart1Sample() {
    assertEquals("CABDFE", solvePart1(SAMPLE_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(1017, solvePart2(MAIN_INPUT));
  }

  private String solvePart1(String name) {
    List<String> input = Util.readResource(name);

    Set<String> included = new TreeSet<>();
    Map<String, Set<String>> dependencies = new HashMap<>();

    for (String s : input) {
      Matcher matcher = PATTERN.matcher(s);
      if (!matcher.matches()) {
        throw new RuntimeException(s);
      }
      String a = matcher.group(1);
      String b = matcher.group(2);
      dependencies.computeIfAbsent(b, ignore -> new TreeSet<>()).add(a);
      included.add(a);
      included.add(b);
    }

    StringBuilder result = new StringBuilder();

    Set<String> used = new HashSet<>();
    while (!used.equals(included)) {
      String best = included.stream().filter(s -> !used.contains(s) && used.containsAll(dependencies.getOrDefault(s, Set.of())))
              .findFirst()
              .get();
      used.add(best);
      result.append(best);
    }
    return result.toString();
  }

  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);

    Set<String> incomplete = new TreeSet<>();
    Map<String, Set<String>> dependencies = new HashMap<>();

    for (String s : input) {
      Matcher matcher = PATTERN.matcher(s);
      if (!matcher.matches()) {
        throw new RuntimeException(s);
      }
      String a = matcher.group(1);
      String b = matcher.group(2);
      dependencies.computeIfAbsent(b, ignore -> new TreeSet<>()).add(a);
      incomplete.add(a);
      incomplete.add(b);
    }

    long t = 0;

    Map<String, Long> progress = new HashMap<>();
    Set<String> completed = new HashSet<>();
    while (!incomplete.isEmpty() || !progress.isEmpty()) {
      while (progress.size() < 5) {
        Optional<String> opt = incomplete.stream().filter(s -> completed.containsAll(dependencies.getOrDefault(s, Set.of())))
                .findFirst();
        if (opt.isEmpty()) {
          break;
        }
        String s = opt.get();
        progress.put(s, t + 61 + (s.charAt(0) - 'A'));
        incomplete.remove(s);
      }
      t = progress.values().stream().mapToLong(Long::longValue).min().getAsLong();
      long completionTime = t;
      Set<String> finished = progress.entrySet().stream()
              .filter(e -> e.getValue() == completionTime)
              .map(Map.Entry::getKey)
              .collect(Collectors.toSet());
      completed.addAll(finished);
      progress.keySet().removeAll(finished);
    }
    return t;
  }

}
