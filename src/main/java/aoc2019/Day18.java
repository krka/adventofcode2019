package aoc2019;

import util.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class Day18 {

  private final List<String> map;
  private final int height;
  private int width;

  public Day18(String name) {
    map = Util.readResource(name);
    while (map.get(map.size() - 1).isEmpty()) {
      map.remove(map.size() - 1);
    }

    height = map.size();
    width = 0;

    for (int row = 0; row < height; row++) {
      String line = map.get(row);
      if (width == 0) {
        width = line.length();
      } else {
        if (width != line.length()) {
          throw new RuntimeException("Invalid map");
        }
      }
      for (int col = 0; col < width; col++) {
        char c = line.charAt(col);
      }
    }
  }

  public int part1() {
    State start = null;
    int targetBitset = 0;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        char c = getMap(row, col);
        if (c == '@') {
          start = new State(row, col, 0, 0, null);
        }
        if (c >= 'a' && c <= 'z') {
          targetBitset |= charToBit(c);
        }
      }
    }
    return bfs(start, targetBitset);
  }

  private int bfs(State start, int targetBitset) {
    Queue<State> queue = new LinkedList<>();
    Set<State> visited = new HashSet<>();
    queue.add(start);
    visited.add(start);
    while (!queue.isEmpty()) {
      State state = queue.poll();
      if (state.keyBitset == targetBitset) {

        // debug
        //ArrayList<State> steps = collectSteps(state);
        //Collections.reverse(steps);
        //steps.forEach(System.out::println);

        return state.steps;
      }

      tryVisit(visited, queue, state, -1, 0, targetBitset);
      tryVisit(visited, queue, state, 1, 0, targetBitset);
      tryVisit(visited, queue, state, 0, -1, targetBitset);
      tryVisit(visited, queue, state, 0, 1, targetBitset);
    }
    throw new RuntimeException("Could not find a solution");
  }

  public int part2() {
    int centerRow = 0;
    int centerCol = 0;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        char c = getMap(row, col);
        if (c == '@') {
          centerCol = col;
          centerRow = row;
        }
      }
    }
    setMap(map, centerRow, centerCol, '#');
    setMap(map, centerRow - 1, centerCol, '#');
    setMap(map, centerRow + 1, centerCol, '#');
    setMap(map, centerRow, centerCol - 1, '#');
    setMap(map, centerRow, centerCol + 1, '#');

    int steps1 = bfs(
            new State(centerRow - 1, centerCol - 1, 0, 0, null),
            getTarget(0, 0, centerRow - 1, centerCol - 1));
    int steps2 = bfs(
            new State(centerRow + 1, centerCol - 1, 0, 0, null),
            getTarget(centerRow + 1, 0, height, centerCol - 1));
    int steps3 = bfs(
            new State(centerRow - 1, centerCol + 1, 0, 0, null),
            getTarget(0, centerCol + 1, centerRow - 1, width));
    int steps4 = bfs(
            new State(centerRow + 1, centerCol + 1, 0, 0, null),
            getTarget(centerRow + 1, centerCol + 1, height, width));
    return steps1 + steps2 + steps3 + steps4;
  }

  private int getTarget(int minRow, int minCol, int maxRow, int maxCol) {
    int targetBitset = 0;
    for (int row = minRow; row <= maxRow; row++) {
      for (int col = minCol; col <= maxCol; col++) {
        char c = getMap(row, col);
        if (c >= 'a' && c <= 'z') {
          targetBitset |= charToBit(c);
        }

      }
    }
    return targetBitset;
  }

  private void setMap(List<String> map, int row, int col, char c) {
    map.set(row, setRow(map.get(row), col, c));
  }

  private String setRow(String s, int col, char c) {
    StringBuilder sb = new StringBuilder(s);
    sb.setCharAt(col, c);
    return sb.toString();
  }

  private ArrayList<State> collectSteps(State state) {
    ArrayList<State> res = new ArrayList<>();
    while (state != null) {
      res.add(state);
      state = state.from;
    }
    return res;
  }

  private void tryVisit(Set<State> visited, Queue<State> queue, State from, int rowdir, int coldir, int targetBitset) {
    State newState = tryMove(from, rowdir, coldir, targetBitset);
    if (newState != null && visited.add(newState)) {
      queue.add(newState);
    }
  }

  private State tryMove(State state, int rowdir, int coldir, int targetBitset) {
    int row = state.row + rowdir;
    int col = state.col + coldir;

    char c = getMap(row, col);
    if (c == '#') {
      return null;
    }
    if (c >= 'A' && c <= 'Z') {
      char key = (char) (c + 32);
      int keybit = charToBit(key);
      if ((targetBitset & keybit) == 0) {
        // This key is in a different part of the map
        return new State(row, col, state.keyBitset, state.steps + 1, state);
      } else if ((state.keyBitset & keybit) != 0) {
        return new State(row, col, state.keyBitset, state.steps + 1, state);
      } else {
        return null;
      }
    }
    if (c >= 'a' && c <= 'z') {
      return new State(row, col, state.keyBitset | charToBit(c), state.steps + 1, state);
    }
    return new State(row, col, state.keyBitset, state.steps + 1, state);
  }

  private char getMap(int row, int col) {
    if (row < 0 || row >= height || col < 0 || col >= width) {
      return '#';
    }
    return map.get(row).charAt(col);
  }

  private int charToBit(char c) {
    int index = c - 'a';
    return (1 << index);
  }

  private class State {
    final int row;
    final int col;
    final int keyBitset;
    final int steps;
    final State from;

    public State(int row, int col, int keyBitset, int steps, State from) {
      this.row = row;
      this.col = col;
      this.keyBitset = keyBitset;
      this.steps = steps;
      this.from = from;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      State state = (State) o;
      return row == state.row &&
              col == state.col &&
              keyBitset == state.keyBitset;
    }

    @Override
    public int hashCode() {
      return Objects.hash(row, col, keyBitset);
    }

    @Override
    public String toString() {
      return "State{" +
              "row=" + row +
              ", col=" + col +
              ", keyBitset=" + Integer.toString(keyBitset, 2) +
              ", steps=" + steps +
              '}';
    }
  }
}
