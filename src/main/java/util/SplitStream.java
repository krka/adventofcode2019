package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

class SplitStream<T> implements Collector<T, SplitStream.Accumulator, List<List<T>>> {
  private final Predicate<T> predicate;
  private final boolean keep;

  public SplitStream(Predicate<T> predicate, boolean keep) {
    this.predicate = predicate;
    this.keep = keep;
  }

  @Override
  public Supplier<Accumulator> supplier() {
    return () -> new Accumulator<>(keep);
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
    private final boolean keep;
    private List<T> current = new ArrayList<>();

    public Accumulator(boolean keep) {
      this.keep = keep;
    }

    Accumulator combiner(Accumulator other) {
      throw new RuntimeException("Combiner is unsupported");
    }

    List<List<T>> finisher() {
      if (!current.isEmpty()) {
        closed.add(current);
      }
      return closed;
    }

    public void add(T value, Predicate<T> predicate) {
      if (predicate.test(value)) {
        if (!current.isEmpty()) {
          closed.add(current);
          current = new ArrayList<>();
        }
        if (keep) {
          current.add(value);
        }
      } else {
        current.add(value);
      }
    }
  }
}
