package util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class WindowsCollector<T> implements Collector<T, WindowsAccumulator<T>, List<List<T>>> {
  private final int size;

  WindowsCollector(final int size) {
    this.size = size;
  }

  @Override
  public Supplier<WindowsAccumulator<T>> supplier() {
    return () -> new WindowsAccumulator<>(size);
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
  private final int size;
  private List<List<T>> tuples = new ArrayList<>();
  private List<T> current = new LinkedList<>();

  WindowsAccumulator(final int size) {
    this.size = size;
  }

  public void add(T value) {
    current.add(value);
    if (current.size() == size) {
      tuples.add(List.copyOf(current));
      current.remove(0);
    }
  }

  public WindowsAccumulator<T> combiner(WindowsAccumulator<T> other) {
    throw new RuntimeException("Combiner unsupported");
  }

  public List<List<T>> finisher() {
    return tuples;
  }
}
