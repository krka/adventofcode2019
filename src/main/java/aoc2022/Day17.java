package aoc2022;

import util.Day;
import util.Util;
import util.Vec2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day17 implements Day {
  private final List<String> input;
  private final String pattern;

  private final List<Set<Vec2>> types = List.of(
          Set.of(Vec2.of(0, 0), Vec2.of(1, 0), Vec2.of(2, 0), Vec2.of(3, 0)),
          Set.of(Vec2.of(1, 0), Vec2.of(0, 1), Vec2.of(1, 1), Vec2.of(2, 1), Vec2.of(1, 2)),
          Set.of(Vec2.of(0, 0), Vec2.of(1, 0), Vec2.of(2, 0), Vec2.of(2, 1), Vec2.of(2, 2)),
          Set.of(Vec2.of(0, 0), Vec2.of(0, 1), Vec2.of(0, 2), Vec2.of(0, 3)),
          Set.of(Vec2.of(0, 0), Vec2.of(1, 0), Vec2.of(0, 1), Vec2.of(1, 1))
          );

  public Day17(String name) {
    input = Util.readResource(name);
    pattern = input.get(0);
  }


  @Override
  public long solvePart1() {
    return solve(2022);
  }

  @Override
  public long solvePart2() {
    return solve(1000000000000L);
  }

  private long solve(long numRocks) {
    Set<Vec2> grid = new HashSet<>();

    int[] width = new int[5];
    int[] height = new int[5];
    for (int i = 0; i < 5; i++) {
      width[i] = 1 + (int) types.get(i).stream().mapToLong(Vec2::getX).max().getAsLong();
      height[i] = 1 + (int) types.get(i).stream().mapToLong(Vec2::getY).max().getAsLong();
    }
    int patternIndex = 0;
    long maxY = 0;

    long extraHeight = 0;

    long[][] prevMax = new long[pattern.length()][5];
    long[][] prevRocks = new long[pattern.length()][5];

    boolean allowSkip = true;
    int skipFirstCycles = 5;
    long addedRocks = 0;

    while (true) {
      if (!(addedRocks < numRocks)) break;
      int typeId = (int) (addedRocks % 5);

      long prevMax2 = prevMax[patternIndex][typeId];
      if (prevMax2 != 0 && allowSkip) {
        final long diffMax = maxY - prevMax2;
        final long diffRocks = addedRocks - prevRocks[patternIndex][typeId];

        //System.out.println("diff max " + diffMax + ", diff rocks " + diffRocks);
        //System.out.println("pattern index " + patternIndex);
        if (skipFirstCycles > 0) {
          skipFirstCycles--;
        } else {
          final long remainingRocks = numRocks - addedRocks;
          long numSkips = remainingRocks / diffRocks;
          addedRocks += numSkips * diffRocks;
          extraHeight += numSkips * diffMax;
          allowSkip = false;
        }
      }
      prevMax[patternIndex][typeId] = maxY;
      prevRocks[patternIndex][typeId] = addedRocks;

      typeId = (int) (addedRocks % 5);

      final Set<Vec2> type = types.get(typeId);

      int maxX = 7 - width[typeId];

      int x = 2;
      long y = maxY + 3;
      while (true) {
        final char moveType = pattern.charAt(patternIndex);
        patternIndex++;
        if (patternIndex == pattern.length()) {
          patternIndex = 0;
        }

        if (moveType == '<') {
          if (x > 0 && !intersects(x - 1, y, type, grid)) {
            x--;
          }
        } else if (moveType == '>') {
          if (x < maxX && !intersects(x + 1, y, type, grid)) {
            x++;
          }
        }

        boolean atFloor = y == 0;
        boolean stop = atFloor || intersects(x, y - 1, type, grid);
        if (stop) {
          for (Vec2 vec2 : type) {
            final Vec2 newVec = vec2.add(x, y);
            grid.add(newVec);
            maxY = (int) Math.max(maxY, 1 + newVec.getY());
          }
          break;
        } else {
          y--;
        }
      }
      addedRocks++;
    }
    return maxY + extraHeight;
  }

  private boolean intersects(int x, long y, Set<Vec2> type, Set<Vec2> grid) {
    for (Vec2 vec2 : type) {
      if (grid.contains(vec2.add(x, y))) {
        return true;
      }
    }
    return false;
  }

}



