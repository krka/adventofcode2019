package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class WindowsCollector<T> implements Collector<T, WindowsAccumulator<T>, List<List<T>>> {
  private final int window;

  WindowsCollector(final int window) {
    this.window = window;
  }

  @Override
  public Supplier<WindowsAccumulator<T>> supplier() {
    return () -> new WindowsAccumulator<>(window);
  }

  @Override
  public BiConsumer<WindowsAccumulator<T>, T> accumulator() {
    return WindowsAccumulator::add;
  }

  @Override
  public BinaryOperator<WindowsAccumulator<T>> combiner() {
    return WindowsAccumulator::combiner;
  }

  @Override
  public Function<WindowsAccumulator<T>, List<List<T>>> finisher() {
    return WindowsAccumulator::finisher;
  }

  @Override
  public Set<Characteristics> characteristics() {
    return Set.of();
  }
}

class WindowsAccumulator<T> {
  private final int window;
  private final List<T> points = new ArrayList<>();

  WindowsAccumulator(final int window) {
    this.window = window;
  }

  public void add(T value) {
    points.add(value);
  }

  public WindowsAccumulator<T> combiner(WindowsAccumulator<T> other) {
    throw new RuntimeException("Combiner unsupported");
  }

  public List<List<T>> finisher() {
    final List<List<T>> tuples = new ArrayList<>();
    int size = points.size();
    for (int i = window; i <= size; i++) {
      tuples.add(points.subList(i - window, i));
    }
    return tuples;
  }
}
