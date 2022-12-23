package aoc2022;

import util.Day;
import util.Grid;
import util.Util;
import util.Vec2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day23 implements Day {

  private final Grid<Character> input;
  private final List<List<Vec2>> dirList;
  private final Set<Vec2> elves;

  public Day23(String name) {
    final List<List<String>> collect = Util.readResource(name).stream().collect(Util.splitBy(s -> s.isEmpty()));
    input = Grid.from(collect.get(0), '.', Function.identity());

    dirList = new ArrayList<>();
    dirList.add(List.of(Vec2.NORTH, Vec2.SOUTH, Vec2.WEST, Vec2.EAST));
    dirList.add(List.of(Vec2.SOUTH, Vec2.WEST, Vec2.EAST, Vec2.NORTH));
    dirList.add(List.of(Vec2.WEST, Vec2.EAST, Vec2.NORTH, Vec2.SOUTH));
    dirList.add(List.of(Vec2.EAST, Vec2.NORTH, Vec2.SOUTH, Vec2.WEST));

    elves = input.stream()
            .filter(e -> e.getValue() == '#')
            .map(Grid.Entry::getPos)
            .collect(Collectors.toSet());

  }


  @Override
  public long solvePart1() {
    final AtomicBoolean anyMoved = new AtomicBoolean();
    Set<Vec2> e = elves;
    for (int i = 0; i < 10; i++) {
      e = step(e, dirList.get(i % 4), anyMoved);
    }

    final int minCol = (int) e.stream().mapToLong(Vec2::getX).min().getAsLong();
    final int maxCol = (int) e.stream().mapToLong(Vec2::getX).max().getAsLong();
    final int minRow = (int) e.stream().mapToLong(Vec2::getY).min().getAsLong();
    final int maxRow = (int) e.stream().mapToLong(Vec2::getY).max().getAsLong();

    int rows = maxRow + 1 - minRow;
    int cols = maxCol + 1 - minCol;
    return rows * cols - e.size();
  }

  private Set<Vec2> step(Set<Vec2> elves, List<Vec2> dirs, AtomicBoolean anyMoved) {
    anyMoved.set(false);
    final Map<Vec2, AtomicInteger> counts = new HashMap<>();
    elves.forEach(pos -> {
      if (!alone(pos, elves)) {
        for (Vec2 dir : dirs) {
          var pos2 = pos.add(dir);
          var pos3 = pos2.add(dir.rotateLeft(1));
          var pos4 = pos2.add(dir.rotateRight(1));
          if (!elves.contains(pos2) && !elves.contains(pos3) && !elves.contains(pos4)) {
            counts.computeIfAbsent(pos2, v -> new AtomicInteger()).incrementAndGet();
            break;
          }
        }
      }
    });
    final Set<Vec2> newElves = new HashSet<>();
    elves.forEach(pos -> {
      boolean moved = false;
      if (!alone(pos, elves)) {
        for (Vec2 dir : dirs) {
          var pos2 = pos.add(dir);
          var pos3 = pos2.add(dir.rotateLeft(1));
          var pos4 = pos2.add(dir.rotateRight(1));
          if (!elves.contains(pos2) && !elves.contains(pos3) && !elves.contains(pos4)) {
            if (counts.computeIfAbsent(pos2, v -> new AtomicInteger()).get() == 1) {
              newElves.add(pos2);
              moved = true;
              anyMoved.set(true);
            }
            break;
          }
        }
      }
      if (!moved) {
        newElves.add(pos);
      }
    });

    return newElves;
  }

  private boolean alone(Vec2 pos, Set<Vec2> elves) {
    return Vec2.DIRS_8.stream().map(pos::add).noneMatch(elves::contains);
  }


  @Override
  public long solvePart2() {
    Set<Vec2> e = elves;
    int round = 0;
    final AtomicBoolean anyMoved = new AtomicBoolean();
    while (true) {
      e = step(e, dirList.get(round % 4), anyMoved);
      if (!anyMoved.get()) {
        return round + 1;
      }
      round++;
    }
  }
}

