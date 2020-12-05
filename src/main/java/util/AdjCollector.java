package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class AdjCollector<T> implements Collector<T, AdjAccumulator<T>, List<Pair<T,T>>> {
  @Override
  public Supplier<AdjAccumulator<T>> supplier() {
    return AdjAccumulator::new;
  }

  @Override
  public BiConsumer<AdjAccumulator<T>, T> accumulator() {
    return AdjAccumulator::add;
  }

  @Override
  public BinaryOperator<AdjAccumulator<T>> combiner() {
    return AdjAccumulator::combiner;
  }

  @Override
  public Function<AdjAccumulator<T>, List<Pair<T, T>>> finisher() {
    return AdjAccumulator::finisher;
  }

  @Override
  public Set<Characteristics> characteristics() {
    return Set.of();
  }
}

class AdjAccumulator<T> {
  private List<Pair<T, T>> pairs = new ArrayList<>();
  private T current;

  public void add(T value) {
    if (current != null) {
      pairs.add(Pair.of(current, value));
    }
    current = value;
  }

  public AdjAccumulator<T> combiner(AdjAccumulator<T> other) {
    throw new RuntimeException("Combiner unsupported");
  }

  public List<Pair<T, T>> finisher() {
    return pairs;
  }
}
