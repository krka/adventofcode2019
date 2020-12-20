package util;

import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Rotatable<T extends Rotatable<T>> {
  static <T extends Rotatable<T>> Stream<T> all(T obj) {
    return StreamSupport.stream(iter(obj), false);
  }

  static <T extends Rotatable<T>> Spliterator<T> iter(T obj) {
    return new Spliterator<T>() {
      T cur = obj;
      int i = 0;
      @Override
      public boolean tryAdvance(Consumer<? super T> action) {
        if (i >= 8) {
          return false;
        }
        action.accept(cur);
        i++;
        if (i == 8) {
          // Last step, no need to do anything
        } else if (i == 4) {
          cur = cur.mirror();
        } else {
          cur = cur.rotateLeft();
        }
        return true;
      }

      @Override
      public Spliterator<T> trySplit() {
        return null;
      }

      @Override
      public long estimateSize() {
        return 8;
      }

      @Override
      public int characteristics() {
        return 0;
      }
    };
  }

  static <T extends Rotatable<T>> T rotateUntil(T tile, Predicate<T> predicate) {
    Optional<T> first = all(tile)
            .filter(predicate)
            .findFirst();
    return first.get();
  }

  T rotateLeft();
  T mirror();

  default T rotateRight() {
    return rotateLeft().rotateLeft().rotateLeft();
  }
}
