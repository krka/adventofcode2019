package util;

import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Grid<T> implements Rotatable<Grid<T>> {
  private final Object[] data;
  private final int rows;
  private final int cols;
  private final T defaultValue;

  private Grid(int rows, int cols, T defaultValue, Object[] data) {
    this.data = data;
    this.rows = rows;
    this.cols = cols;
    this.defaultValue = defaultValue;
  }

  private Grid(int rows, int cols, T defaultValue) {
    this(rows, cols, defaultValue, IntStream.rangeClosed(0, rows * cols).mapToObj(value -> defaultValue).toArray());
  }

  public static <T> Grid<T> create(int rows, int cols, T defaultValue) {
    return new Grid<>(rows, cols, defaultValue);
  }

  public static Grid<Character> from(List<String> lines, char defaultValue) {
    return from(lines, defaultValue, x -> x);
  }

    public static <T> Grid<T> from(List<String> lines, T defaultValue, Function<Character, T> mapper) {
    int rows = lines.size();
    int cols = lines.stream().mapToInt(String::length).max().getAsInt();
    Grid<T> grid = Grid.create(rows, cols, defaultValue);

    int row = 0;
    for (String line : lines) {
      for (int col = 0; col < cols; col++) {
        final T c;
        if (col >= line.length()) {
          c = defaultValue;
        } else {
          c = mapper.apply(line.charAt(col));
        }
        grid.set(row, col, c);
      }
      row++;
    }
    return grid;
  }

  public void floodFill(Vec2 pos, Predicate<T> predicate, T newValue) {
    if (!predicate.test(get(pos))) {
      return;
    }
    Stack<Vec2> x = new Stack<>();
    x.push(pos);
    while (!x.isEmpty()) {
      final Vec2 pos2 = x.pop();
      Vec2.DIRS.stream().forEach(vec2 -> {
        final Vec2 nextPos = vec2.add(pos2);
        if (inbound(nextPos) && predicate.test(get(nextPos))) {
          set(nextPos, newValue);
          x.push(nextPos);
        }
      });
    }
  }

  public T getDefaultValue() {
    return defaultValue;
  }

  public void setGrid(int row, int col, Grid<T> source, int offsetRow, int offsetCol, int rows, int cols) {
    for (int sourceRow = 0; sourceRow < rows; sourceRow++) {
      for (int sourceCol = 0; sourceCol < cols; sourceCol++) {
        set(row + sourceRow, col + sourceCol, source.get(sourceRow + offsetRow, sourceCol + offsetCol));
      }
    }
  }

  public void set(int row, int col, T value) {
    if (inbound(row, col)) {
      data[row * cols + col] = value;
    } else {
      throw new RuntimeException("Out of bounds: " + row + ", " + col);
    }
  }

  public void set(long row, long col, T value) {
    set((int) row, (int) col, value);
  }

  public void set(Vec2 pos, T value) {
    set(pos.getY(), pos.getX(), value);
  }

  public T get(int row, int col, T defaultValue) {
    if (inbound(row, col)) {
      return (T) data[row * cols + col];
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

  public T get(Vec2 v) {
    return get(v.getY(), v.getX());
  }

  public boolean inbound(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }

  public boolean inbound(long row, long col) {
    return inbound((int) row, (int) col);
  }

  public boolean inbound(Vec2 v) {
    return inbound(v.getY(), v.getX());
  }

  public Grid<T> duplicate() {
    return new Grid<>(rows, cols, defaultValue, data.clone());
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

  public Stream<T> values() {
    return stream().map(Entry::getValue);
  }

  public Stream<Entry<T>> stream() {
    return StreamSupport.stream(new Spliterator<>() {
      int row = 0;
      int col = 0;
      @Override
      public boolean tryAdvance(Consumer<? super Entry<T>> action) {
        if (row >= rows) {
          return false;
        }
        int r = row;
        int c = col;
        col++;
        if (col >= cols) {
          col = 0;
          row++;
        }
        action.accept(new Entry<>(r, c, get(r, c), Grid.this));
        return true;
      }

      @Override
      public Spliterator<Entry<T>> trySplit() {
        return null;
      }

      @Override
      public long estimateSize() {
        return data.length;
      }

      @Override
      public int characteristics() {
        return Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.NONNULL
                | Spliterator.IMMUTABLE;
      }
    }, false);
  }

  public void forEachNeighbour(int row, int col, GridConsumer<T> consumer) {
    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        if (i == 0 && j == 0) {
          continue;
        }
        int r = row + i;
        int c = col + j;
        if (inbound(r, c)) {
          consumer.accept(r, c, get(r, c));
        }
      }
    }
  }

  public long count(Predicate<T> predicate) {
    return values().filter(predicate).count();
  }

  @Override
  public Grid<T> rotateLeft() {
    Grid<T> newGrid = new Grid<>(cols, rows, defaultValue, new Object[data.length]);
    forEach((row, col, value) -> {
      newGrid.set(cols - col - 1, row, value);
    });
    return newGrid;
  }

  @Override
  public Grid<T> mirrorHoriz() {
    Grid<T> newGrid = new Grid<>(rows, cols, defaultValue, new Object[data.length]);
    forEach((row, col, value) -> {
      newGrid.set(rows - row - 1, col, value);
    });
    return newGrid;
  }

  public Stream<Entry<T>> edgeStream() {
    return stream().filter(e -> 0 == e.getCol() || 0 == e.getRow());
  }

  public void swap(Vec2 a, Vec2 b) {
    final T val1 = get(a);
    final T val2 = get(b);
    set(a, val2);
    set(b, val1);
  }

  public interface GridConsumer<T> {
    void accept(int row, int col, T value);
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  public static class Entry<T> {
    private final int row;
    private final int col;
    private final T value;
    private final Grid<T> grid;

    public Entry(int row, int col, T value, Grid<T> grid) {
      this.row = row;
      this.col = col;
      this.value = value;
      this.grid = grid;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Entry<?> entry = (Entry<?>) o;
      return row == entry.row &&
              col == entry.col &&
              value.equals(entry.value) &&
              grid.equals(entry.grid);
    }

    @Override
    public int hashCode() {
      return Objects.hash(row, col, value, grid);
    }

    @Override
    public String toString() {
      return "Entry{" +
              "row=" + row +
              ", col=" + col +
              ", value=" + value +
              '}';
    }

    public int getRow() {
      return row;
    }

    public int getCol() {
      return col;
    }

    public Vec2 getPos() {
      return new Vec2(getCol(), getRow());
    }

    public T getValue() {
      return value;
    }

    public Grid<T> getGrid() {
      return grid;
    }
  }
}
