package aoc2019;

import util.Util;
import util.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

public class Day20 {

  private final List<String> map;
  private final int height;
  private int width;
  private final Map<String, List<Vec3>> tags = new HashMap<>();
  private final Map<Vec3, String> reversed = new HashMap<>();

  public Day20(String name) {
    map = Util.readResource(name);
    while (map.get(map.size() - 1).isEmpty()) {
      map.remove(map.size() - 1);
    }

    height = map.size();
    width = 0;

    for (int row = 0; row < height; row++) {
      String line = map.get(row);
      width = Math.max(width, line.length());
    }

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        char c = getMap(row, col);
        if (c >= 'A' && c <= 'Z') {
          List<Vec3> adjacent = findAdjacent(row, col, '.');
          addTags(c, row + 1, col, adjacent);
          addTags(c, row, col + 1, adjacent);
        }
      }
    }
  }

  private char getMap(int row, int col) {
    if (row < 0 || row >= height || col < 0 || col >= width) {
      return ' ';
    }
    String line = map.get(row);
    if (col >= line.length()) {
      return ' ';
    }
    return line.charAt(col);
  }

  private void addTags(char c, int row, int col, List<Vec3> adjacent) {
    char c2 = getMap(row, col);
    if (c2 >= 'A' && c2 <= 'Z') {
      List<Vec3> adjacent2 = findAdjacent(row, col, '.');
      HashSet<Vec3> vector3s = new HashSet<>();
      vector3s.addAll(adjacent);
      vector3s.addAll(adjacent2);
      String tag = "" + c + c2;
      if (vector3s.size() != 1) {
        throw new RuntimeException("" + tag + ": " + vector3s);
      }
      tags.computeIfAbsent(tag, ignore -> new ArrayList<>()).addAll(vector3s);
      vector3s.forEach(vector3 -> reversed.put(vector3, tag));
    }
  }

  private List<Vec3> findAdjacent(int row, int col, char c) {
    List<Vec3> res = new ArrayList<>();
    tryAddAdj(res, row - 1, col, c);
    tryAddAdj(res, row + 1, col, c);
    tryAddAdj(res, row, col - 1, c);
    tryAddAdj(res, row, col + 1, c);
    return res;
  }

  private void tryAddAdj(List<Vec3> res, int row, int col, char c) {
    if (getMap(row, col) == c) {
      res.add(new Vec3(row, col, 0));
    }
  }

  private int bfs(State start, Function<Vec3, Integer> levelFunc) {
    Queue<State> queue = new LinkedList<>();
    Set<State> visited = new HashSet<>();
    queue.add(start);
    visited.add(start);
    while (!queue.isEmpty()) {
      State state = queue.poll();

      String tag = reversed.get(state.v);
      if (tag != null) {

        if (state.level == 0 && tag.equals("ZZ")) {
          return state.steps;
        }

        List<Vec3> vector3s = tags.get(tag);
        for (Vec3 dest : vector3s) {
          if (!dest.equals(state.v)) {

            int newLevel = state.level + levelFunc.apply(state.v);
            if (newLevel >= 0) {
              State newState = new State(dest, newLevel, state.steps + 1, state);
              if (visited.add(newState)) {
                queue.add(newState);
              }
            }
          }
        }
      }

      tryVisit(visited, queue, state, -1, 0);
      tryVisit(visited, queue, state, 1, 0);
      tryVisit(visited, queue, state, 0, -1);
      tryVisit(visited, queue, state, 0, 1);
    }
    throw new RuntimeException("Could not find a solution");
  }

  private int getLevel(Vec3 v) {
    if (v.getX() <= 5 || v.getY() <= 5 || v.getX() >= height - 5 || v.getY() >= width - 5) {
      return -1;
    } else {
      return 1;
    }
  }

  private void tryVisit(Set<State> visited, Queue<State> queue, State state, int rowdiff, int coldiff) {
    State newState = tryMove(state, rowdiff, coldiff);
    if (newState != null && visited.add(newState)) {
      queue.add(newState);
    }
  }

  public int part1() {
    Vec3 start = tags.get("AA").get(0);
    return bfs(new State(start, 0, 0, null), v -> 0);
  }

  public int part2() {
    Vec3 start = tags.get("AA").get(0);
    return bfs(new State(start, 0, 0, null), this::getLevel);
  }

  private State tryMove(State state, int rowdir, int coldir) {
    int row = (int) (state.v.getX() + rowdir);
    int col = (int) (state.v.getY() + coldir);

    char c = getMap(row, col);
    if (c == '.') {
      return new State(row, col, state.level, state.steps + 1, state);
    }
    return null;
  }

  private class State {
    final Vec3 v;
    final int level;
    final int steps;
    final State from;

    public State(int row, int col, int level, int steps, State from) {
      this.v = new Vec3(row, col, 0);
      this.level = level;
      this.steps = steps;
      this.from = from;
    }

    public State(Vec3 v, int level, int steps, State from) {
      this.v = v;
      this.level = level;
      this.steps = steps;
      this.from = from;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      State state = (State) o;
      return level == state.level &&
              v.equals(state.v);
    }

    @Override
    public int hashCode() {
      return Objects.hash(v, level);
    }

    @Override
    public String toString() {
      return "State{" +
              "v=" + v +
              ", level=" + level +
              ", steps=" + steps +
              '}';
    }
  }

}
