package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class Partitions<T> implements Collector<T, Partitions.Accumulator, List<List<T>>> {
  private final BiFunction<T, T, Boolean> predicate;

  public Partitions(BiFunction<T, T, Boolean> predicate) {
    this.predicate = predicate;
  }

  @Override
  public Supplier<Accumulator> supplier() {
    return Accumulator::new;
  }

  @Override
  public BiConsumer<Accumulator, T> accumulator() {
    return (partition, t) -> partition.add(t, predicate);
  }

  @Override
  public BinaryOperator<Accumulator> combiner() {
    return Accumulator::combiner;
  }

  @Override
  public Function<Accumulator, List<List<T>>> finisher() {
    return partition -> partition.finisher();
  }

  @Override
  public Set<Characteristics> characteristics() {
    return Set.of();
  }

  static class Accumulator<T> {

    private final List<List<T>> closed = new ArrayList<>();
    private List<T> current = new ArrayList<>();
    private T last;

    Accumulator combiner(Accumulator other) {
      throw new RuntimeException("Combiner is unsupported");
    }

    List<List<T>> finisher() {
      if (last != null) {
        current.add(last);
      }
      if (!current.isEmpty()) {
        closed.add(current);
      }
      return closed;
    }

    public void add(T value, BiFunction<T, T, Boolean> predicate) {
      if (last == null) {
        last = value;
      } else {
        current.add(last);
        if (!predicate.apply(last, value)) {
          closed.add(current);
          current = new ArrayList<>();
        }
        last = value;
      }
    }
  }
}
