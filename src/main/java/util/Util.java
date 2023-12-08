package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.fail;

public class Util {
  private static final ClassLoader CLASS_LOADER = Util.class.getClassLoader();

  public static Reader fromResource(String name) {
    InputStream stream = CLASS_LOADER.getResourceAsStream(name);
    if (stream == null) {
      throw new IllegalArgumentException("Resource not found: " + name);
    }
    return new InputStreamReader(stream, StandardCharsets.UTF_8);
  }

  public static List<BigInteger> toBigInt(List<Integer> list) {
    return list.stream().map(BigInteger::valueOf).collect(Collectors.toList());
  }

  public static List<BigInteger> toBigIntFromLong(List<Long> list) {
    return list.stream().map(BigInteger::valueOf).collect(Collectors.toList());
  }

  public static List<String> readResource(String name) {
    List<String> list = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(fromResource(name))) {
      while (true) {
        String s = reader.readLine();
        if (s == null) {
          break;
        }
        list.add(s);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return list;
  }

  public static void deleteFromEnd(List<?> list, int index) {
    while (list.size() > index) {
      list.remove(list.size() - 1);
    }
  }

  public static String toString(List<BigInteger> intCode) {
    return intCode.stream().map(Object::toString).collect(Collectors.joining(","));
  }

  public static <T> List<List<T>> partition(List<T> list, int size) {
    if (size < 1) {
      throw new IllegalArgumentException();
    }
    final List<List<T>> result = new ArrayList<>();
    int i = 0;
    while (true) {
      int nextI = i + size;
      if (nextI >= list.size()) {
        result.add(list.subList(i, list.size()));
        break;
      } else {
        result.add(list.subList(i, nextI));
      }
      i = nextI;
    }
    return result;
  }

  public static <T> Collector<T, ?, List<List<T>>> toPartitions(BiFunction<T, T, Boolean> predicate) {
    return new Partitions<>(predicate);
  }

  public static <T> Collector<T, ?, List<List<T>>> splitBy(Predicate<T> predicate) {
    return new SplitStream<>(predicate, false);
  }

  public static <T> Collector<T, ?, List<List<T>>> splitBefore(Predicate<T> predicate) {
    return new SplitStream<>(predicate, true);
  }

  public static <T> WindowsCollector<T> windows(final int window) {
    return new WindowsCollector<>(window);
  }

  public static IntBinaryOperator exactlyOneInt() throws RuntimeException {
    return (left, right) -> {
      throw new RuntimeException();
    };
  }

  public static LongBinaryOperator exactlyOneLong() throws RuntimeException {
    return (left, right) -> {
      throw new RuntimeException();
    };
  }

  public static <T> BinaryOperator<T> exactlyOne() throws RuntimeException {
    return (left, right) -> {
      throw new RuntimeException();
    };
  }

  public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
    Set<T> ans = new HashSet<>();
    ans.addAll(a);
    ans.retainAll(b);
    return ans;
  }

  public static <T> Set<T> union(Set<T> a, Set<T> b) {
    Set<T> ans = new HashSet<>();
    ans.addAll(a);
    ans.addAll(b);
    return ans;
  }

  public static <T> Set<T> diff(Set<T> a, Set<T> b) {
    Set<T> ans = new HashSet<>();
    ans.addAll(a);
    ans.removeAll(b);
    return ans;
  }

  public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
    Map<V, K> rev = new HashMap<>();
    map.forEach((key, value) -> {
      if (rev.containsKey(value)) {
        throw new RuntimeException();
      }
      rev.put(value, key);
    });
    return rev;
  }

  public static <A, B> List<Pair<A, B>> zip(List<A> a, List<B> b) {
    int size = Math.min(a.size(), b.size());
    ArrayList<Pair<A, B>> res = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      res.add(Pair.of(a.get(i), b.get(i)));
    }
    return res;
  }

  public static <A> List<Pair<A, Integer>> zipWithIndex(List<A> a) {
    int size = a.size();
    ArrayList<Pair<A, Integer>> res = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      res.add(Pair.of(a.get(i), i));
    }
    return res;
  }

  public static List<Character> toList(String s) {
    return s.chars().mapToObj(i -> (char) i).collect(Collectors.toList());
  }

  public static Set<Character> toSet(String s) {
    return s.chars().mapToObj(i -> (char) i).collect(Collectors.toSet());
  }

  public static void assertTooHigh(long actual, long tooHigh) {
    if (actual >= tooHigh) {
      fail(actual + " is too high, known too-high is " + tooHigh);
    }
  }

  public static void assertTooLow(long actual, long tooLow) {
    if (actual <= tooLow) {
      fail(actual + " is too low, known too-low is " + tooLow);
    }
  }

  public static <T> Comparator<T> comparingEnum(ToEnumFunction<? super T> keyExtractor) {
    Objects.requireNonNull(keyExtractor);
    return Comparator.comparingInt(c -> keyExtractor.applyAsEnum(c).ordinal());
  }

  public static <T extends Enum<?>> Comparator<T> comparingEnum() {
    return Comparator.comparingInt(c -> c.ordinal());
  }

  public static <T, U extends Comparable<U>> Comparator<T> comparingList(Function<T, List<U>> keyExtractor) {
    return (o1, o2) -> {
      final List<U> list1 = keyExtractor.apply(o1);
      final List<U> list2 = keyExtractor.apply(o2);
      int n = Math.min(list1.size(), list2.size());
      for (int i = 0; i < n; i++) {
        final U v1 = list1.get(i);
        final U v2 = list2.get(i);
        final int comp = v1.compareTo(v2);
        if (comp != 0) {
          return comp;
        }
      }
      return list2.size() - list1.size();
    };
  }

  public static long gcd(long n1, long n2) {
    if (n2 == 0) {
      return n1;
    }
    return gcd(n2, n1 % n2);
  }

  public static long lcm(long a, long b) {
    return a * b / gcd(a, b);
  }

  public static List<Long> factors(long value) {
    if (value < 0) {
      return Stream.of(List.of(-1L), factors(-value))
              .flatMap(Collection::stream)
              .collect(Collectors.toList());
    }
    if (value <= 3) {
      return List.of(value);
    }
    final ArrayList<Long> res = new ArrayList<>();
    while (0 == (value & 1)) {
      res.add(2L);
      value >>= 1;
    }
    for (long candidate = 3; candidate*candidate <= value; candidate += 2) {
      if (0 == (value % candidate)) {
        value /= candidate;
        res.add(candidate);
      }
    }
    if (value > 1) {
      res.add(value);
    }
    return res;
  }

  public interface ToEnumFunction<T> {
    Enum<?> applyAsEnum(T obj);
  }
}
