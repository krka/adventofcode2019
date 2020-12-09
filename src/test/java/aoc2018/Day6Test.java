package aoc2018;

import org.junit.Test;
import util.Util;
import util.Vec2;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Day6Test {

  private static final List<Vec2> DIRS = Stream
          .of("1,0 -1,0 0,-1 0,1".split(" "))
          .map(Vec2::parse).collect(Collectors.toList());

  @Test
  public void testPart1() {
    assertEquals(4233, solvePart1("2018/day6.in"));
  }

  @Test
  public void testPart1Sample() {
    assertEquals(17, solvePart1("2018/day6-sample.in"));
  }

  private long solvePart1(String name) {
    Set<Vec2> roots = Util.readResource(name).stream().map(Vec2::parse).collect(Collectors.toSet());

    long minX = roots.stream().mapToLong(Vec2::getX).min().getAsLong();
    long maxX = roots.stream().mapToLong(Vec2::getX).max().getAsLong();
    long minY = roots.stream().mapToLong(Vec2::getY).min().getAsLong();
    long maxY = roots.stream().mapToLong(Vec2::getY).max().getAsLong();

    Map<Vec2, AtomicInteger> counts = new HashMap<>();
    for (Vec2 root : roots) {
      counts.put(root, new AtomicInteger(1));
    }

    Map<Vec2, Set<Vec2>> next = new HashMap<>();
    for (Vec2 root : roots) {
      next.put(root, Set.of(root));
    }

    Set<Vec2> visited = new HashSet<>(roots);

    while (!next.keySet().stream().allMatch(v -> outOfbounds(v, minX, maxX, minY, maxY))) {
      step(counts, next, visited);
    }

    Set<Vec2> infinite = next.values()
            .stream()
            .filter(owners -> owners.size() == 1)
            .flatMap(Collection::stream).collect(Collectors.toSet());

    counts.keySet().removeAll(infinite);

    return counts.values().stream().mapToInt(AtomicInteger::get).max().getAsInt();
  }

  private void step(Map<Vec2, AtomicInteger> counts, Map<Vec2, Set<Vec2>> next, Set<Vec2> visited) {
    Map<Vec2, Set<Vec2>> candidates = new HashMap<>();

    for (Map.Entry<Vec2, Set<Vec2>> entry : next.entrySet()) {
      Vec2 pos = entry.getKey();
      Set<Vec2> owners = entry.getValue();
      for (Vec2 dir : DIRS) {
        Vec2 newPos = pos.add(dir);
        if (!visited.contains(newPos)) {
          candidates.computeIfAbsent(newPos, ignore -> new HashSet<>()).addAll(owners);
        }
      }
    }

    next.clear();

    for (Map.Entry<Vec2, Set<Vec2>> entry : candidates.entrySet()) {
      Vec2 pos = entry.getKey();
      Set<Vec2> owners = entry.getValue();
      next.put(pos, owners);
      visited.add(pos);
      if (owners.size() == 1) {
        Vec2 owner = owners.iterator().next();
        counts.get(owner).incrementAndGet();
      }
    }
  }

  private boolean outOfbounds(Vec2 v, long minX, long maxX, long minY, long maxY) {
    return v.getX() < minX || v.getX() > maxX || v.getY() < minY || v.getY() > maxY;
  }


  @Test
  public void testPart2() {
    assertEquals(-1, solvePart2());
  }

  private int solvePart2() {
    return -1;
  }


}