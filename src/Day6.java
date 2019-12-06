import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day6 {
  private final HashMap<String, String> orbits = new HashMap<>();
  private final HashMap<String, Integer> distances = new HashMap<>();

  public static void main(String[] args) throws IOException {
    System.out.println(new Day6("day6-sample.in").part1());
    System.out.println(new Day6("day6.in").part1());
    System.out.println(new Day6("day6-2-sample.in").part2());
    System.out.println(new Day6("day6.in").part2());
  }

  public Day6(String filename) throws IOException {
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filename)))) {
      while (true) {
        String line = bufferedReader.readLine();
        if (line == null) {
          break;
        }
        String[] split = line.split("\\)");
        if (split.length == 2) {
          orbits.put(split[1], split[0]);
        }
      }
    }
  }

  private int part1() {
    return orbits.keySet().stream().mapToInt(this::count).sum();
  }

  private int part2() {
    Map<String, Integer> visited = new HashMap<>();

    String current = "YOU";
    int i = 0;
    while (current != null) {
      visited.put(current, i);
      i++;
      current = orbits.get(current);
    }

    current = "SAN";
    i = 0;
    while (current != null) {
      Integer integer = visited.get(current);
      if (integer != null) {
        return i + integer - 2;
      }
      i++;
      current = orbits.get(current);
    }
    throw new RuntimeException("No solution found");
  }

  private int count(String orbitter) {
    Integer d = distances.get(orbitter);
    if (d != null) {
      return d;
    }
    String around = orbits.get(orbitter);
    if (around == null) {
      distances.put(orbitter, 0);
      return 0;
    }
    int distance = 1 + count(around);
    distances.put(orbitter, distance);
    return distance;
  }
}
