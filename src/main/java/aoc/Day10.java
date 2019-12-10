package aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day10 {
  private final boolean[][] data;
  private final int height;
  private final int width;

  public Day10(String name) {
    List<String> lines = new ArrayList<>();
    try {
      try (BufferedReader reader = new BufferedReader(Util.fromResource(name))) {
        while (true) {
          String line = reader.readLine();
          if (line == null || line.isEmpty()) {
            break;
          }
          lines.add(line);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    height = lines.size();
    width = lines.get(0).length();
    data = new boolean[height][width];
    for (int row = 0; row < lines.size(); row++) {
      String s = lines.get(row);
      for (int i = 0; i < s.length(); i++) {
        data[row][i] = s.charAt(i) == '#';
      }
    }
  }

  int solve() {
    int best = -1;
    int bestRow = -1;
    int bestCol = -1;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        if (data[row][col]) {
          List<Point> ordering = getOrdering(col, row);
          long count = ordering.stream().filter(point -> point.getDistance() == 0).count();
          if (count > best) {
            bestRow = row;
            bestCol = col;
            best = (int) count;
          }
        }
      }
    }
    System.out.println("Best is at col: " + bestCol + ", row: " + bestRow);
    return best;
  }

  public List<Point> getOrdering(int col, int row) {
    TreeMap<Double, TreeSet<Point>> points = new TreeMap<>();

    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        if (data[r][c]) {
          int rowDiff = row - r;
          int colDiff = col - c;
          if (rowDiff != 0 || colDiff != 0) {
            double angle;
            if (colDiff == 0 && rowDiff > 0) {
              angle = -Math.PI;
            } else {
              angle = Math.atan2(colDiff, -rowDiff);
            }
            int distance = rowDiff * rowDiff + colDiff * colDiff;
            Point point = new Point(r, c, distance, angle);
            points.computeIfAbsent(angle, a -> new TreeSet<>(Comparator.comparingInt(Point::getDistance))).add(point);
          }
        }
      }
    }

    List<Point> order = points.values().stream()
            .flatMap(e -> distanceToOrder(e).stream())
            .sorted(Comparator.comparingInt(Point::getDistance).thenComparingDouble(Point::getAngle))
            .collect(Collectors.toList());

    return order;
  }

  private List<Point> distanceToOrder(TreeSet<Point> value) {
    ArrayList<Point> res = new ArrayList<>();
    int i = 0;
    for (Point point : value) {
      res.add(point.withDistance(i));
      i++;
    }
    return res;
  }

  static class Point {
    final int row;
    final int col;
    private final int distance;
    private final double angle;

    Point(int row, int col, int distance, double angle) {
      this.row = row;
      this.col = col;
      this.distance = distance;
      this.angle = angle;
    }

    int getDistance() {
      return distance;
    }

    public double getAngle() {
      return angle;
    }

    @Override
    public String toString() {
      return "Point{" +
              "row=" + row +
              ", col=" + col +
              ", distance=" + distance +
              ", val=" + angle +
              '}';
    }

    public Point withDistance(int distance) {
      return new Point(row, col, distance, angle);
    }
  }
}
