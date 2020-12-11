package util;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Grid<T> {
  private final Object[][] data;
  private final int rows;
  private final int cols;
  private final T defaultValue;

  public Grid(int rows, int cols, T defaultValue) {
    this.rows = rows;
    this.cols = cols;
    this.defaultValue = defaultValue;
    this.data = new Object[rows][cols];
  }

  public static <T> Grid<T> from(List<String> lines, T defaultValue, Function<Character, T> mapper) {
    int rows = lines.size();
    String first = lines.get(0);
    int cols = first.length();
    Grid<T> grid = new Grid<T>(rows, cols, defaultValue);

    int row = 0;
    for (String line : lines) {
      for (int col = 0; col < cols; col++) {
        grid.set(row, col, mapper.apply(line.charAt(col)));
      }
      row++;
    }
    return grid;
  }

  public void set(int row, int col, T value) {
    if (inbound(row, col)) {
      data[row][col] = value;
    } else {
      throw new RuntimeException("Out of bounds: " + row + ", " + col);
    }
  }

  public T get(int row, int col, T defaultValue) {
    if (inbound(row, col)) {
      return (T) data[row][col];
    } else {
      return defaultValue;
    }
  }

  public T get(int row, int col) {
    return get(row, col, defaultValue);
  }

  public T get(long row, long col) {
    return get((int) row, (int) col);
  }

    private boolean inbound(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }

  public Grid<T> duplicate() {
    Grid<T> newGrid = new Grid<>(rows, cols, defaultValue);
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        T val = get(row, col);
        newGrid.set(row, col, val);
      }
    }
    return newGrid;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        sb.append(get(row, col).toString());
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  public int rows() {
    return rows;
  }

  public int cols() {
    return cols;
  }

  public void forEach(GridConsumer<T> consumer) {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        consumer.accept(row, col, get(row, col));
      }
    }
  }

  public int count(Predicate<T> predicate) {
    int count = 0;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        count += predicate.test(get(row, col)) ? 1 : 0;
      }
    }
    return count;
  }

  public interface GridConsumer<T> {
    void accept(int row, int col, T value);
  }
}
