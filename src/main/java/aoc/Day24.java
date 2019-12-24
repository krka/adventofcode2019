package aoc;

import util.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Day24 {

  private int state;

  public Day24(String name) {
    List<String> grid = Util.readResource(name);
    state = 0;
    for (int i = 0; i < 25; i++) {
      int row = i / 5;
      int col = i % 5;
      char c = grid.get(row).charAt(col);
      if (c == '#') {
        state = state | (1 << i);
      }
    }

  }

  public int part1() {
    Set<Integer> visited = new HashSet<>();
    while (true) {
      //printGrid(state);
      if (!visited.add(state)) {
        return state;
      }
      state = newState(state);
    }
  }

  private void printGrid(int state) {
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        System.out.print(bitSet(state, 5 * row + col) ? "#" : '.');
      }
      System.out.println();
    }
    System.out.println(Integer.toString(state, 2));
    System.out.println();
  }

  private int newState(int state) {
    int res = 0;
    for (int i = 0; i < 25; i++) {
      int neighbours = neighbours(state, i);
      if (bitSet(state, i)) {
        if (neighbours == 1) {
          res |= (1 << i);
        }
      } else {
        if (neighbours == 1 || neighbours == 2) {
          res |= (1 << i);
        }
      }
    }
    return res;
  }

  private static boolean bitSet(int state, int i) {
    if (i < 0) {
      return false;
    }
    if (i > 30) {
      return false;
    }
    return 0 != (state & (1 << i));
  }

  private int neighbours(int state, int i) {
    int c = 0;
    if ((i % 5) != 0 && bitSet(state, i - 1)) {
      c++;
    }
    if ((i % 5) != 4 && bitSet(state, i + 1)) {
      c++;
    }
    if (bitSet(state, i - 5)) {
      c++;
    }
    if (bitSet(state, i + 5)) {
      c++;
    }
    return c;
  }

  public int part2() {
    TreeMap<Integer, Integer> levels = new TreeMap<>();
    levels.put(0, state);
    for (int i = 0; i < 200; i++) {
      levels = simulate(levels);
    }
    int sum = 0;
    for (Integer value : levels.values()) {
      sum += numBits(value);
    }
    return sum;
  }

  private TreeMap<Integer, Integer> simulate(TreeMap<Integer, Integer> levels) {
    TreeMap<Integer, Integer> res = new TreeMap<>();
    maybeAdd(levels, levels.lastEntry().getKey(), levels.lastEntry().getValue());
    maybeAdd(levels, levels.firstEntry().getKey(), levels.firstEntry().getValue());
    for (Map.Entry<Integer, Integer> entry : levels.entrySet()) {
      int level = entry.getKey();
      int state = entry.getValue();
      int inner = levels.getOrDefault(level + 1, 0);
      int outer = levels.getOrDefault(level - 1, 0);

      int newState = getNewState(state, inner, outer);
      res.put(level, newState);
    }

    return res;
  }

  private int getNewState(int state, int inner, int outer) {
    int res = 0;
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        if (row == 2 && col == 2) {
          continue;
        }
        int i = 5 * row + col;
        int neighbours = neighbours(state, i);
        if (row == 0) {
          neighbours += bitSet(outer, 7) ? 1 : 0;
        }
        if (row == 4) {
          neighbours += bitSet(outer, 17) ? 1 : 0;
        }
        if (col == 0) {
          neighbours += bitSet(outer, 11) ? 1 : 0;
        }
        if (col == 4) {
          neighbours += bitSet(outer, 13) ? 1 : 0;
        }
        if (i == 7) {
          for (int j = 0; j < 5; j++) {
            neighbours += bitSet(inner, j) ? 1 : 0;
          }
        }
        if (i == 17) {
          for (int j = 0; j < 5; j++) {
            neighbours += bitSet(inner, 20 + j) ? 1 : 0;
          }
        }
        if (i == 11) {
          for (int j = 0; j < 5; j++) {
            neighbours += bitSet(inner, 5 * j) ? 1 : 0;
          }
        }
        if (i == 13) {
          for (int j = 0; j < 5; j++) {
            neighbours += bitSet(inner, 4 + 5 * j) ? 1 : 0;
          }
        }
        if (bitSet(state, i)) {
          if (neighbours == 1) {
            res |= (1 << i);
          }
        } else {
          if (neighbours == 1 || neighbours == 2) {
            res |= (1 << i);
          }
        }
      }
    }
    return res;
  }

  private void maybeAdd(TreeMap<Integer, Integer> res, int level, int state) {
    if (state != 0) {
      res.putIfAbsent(level - 1, 0);
      res.putIfAbsent(level + 1, 0);
    }
  }

  private int numBits(int value) {
    int c = 0;
    while (value > 0) {
      c += (value & 1);
      value >>= 1;
    }
    return c;
  }
}
