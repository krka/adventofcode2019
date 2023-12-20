package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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

  public static String removePrefix(String prefix, String s) {
    if (s.startsWith(prefix)) {
      return s.substring(prefix.length());
    }
    throw new RuntimeException("Expected '" + s + "' to start with '" + prefix + "'");
  }

  public static String removeSuffix(String suffix, String s) {
    if (s.endsWith(suffix)) {
      return s.substring(0, s.length() - suffix.length());
    }
    throw new RuntimeException("Expected '" + s + "' to end with '" + suffix + "'");
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

  public static List<String> split(String pattern, String s) {
    return Splitter.withDelim(pattern).split(s);
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
    if (a == 0 || b == 0) {
      return 0;
    }
    return a * b / gcd(a, b);
  }

  //  return [d, a, b] such that d = gcd(p, q), ap + bq = d
  public static GcdResult gcdExtended(long p, long q) {
    if (q == 0) {
      return new GcdResult(p, 1, 0);
    }

    GcdResult vals = gcdExtended(q, p % q);
    long d = vals.d;
    long a = vals.b;
    long b = vals.a - (p / q) * vals.b;
    return new GcdResult(d, a, b);
  }

  public static boolean even(int x) {
    return 0 == (x & 1);
  }

  public static <A, B> List<Pair<A, B>> cross(List<A> list1, List<B> list2) {
    final ArrayList<Pair<A, B>> res = new ArrayList<>();
    for (A a : list1) {
      for (B b : list2) {
        res.add(Pair.of(a, b));
      }
    }
    return res;
  }

  public static <A> List<Pair<A, A>> distinctPairs(List<A> list1) {
    final ArrayList<Pair<A, A>> res = new ArrayList<>();
    final int len = list1.size();
    for (int i = 0; i < len; i++) {
      final A a = list1.get(i);
      for (int j = i+1; j < len; j++) {
        final A b = list1.get(j);
        res.add(Pair.of(a, b));
      }
    }
    return res;
  }

  public static long[] newLongArray(int size, long value) {
    final long[] res = new long[size];
    Arrays.fill(res, value);
    return res;
  }

  /**
   *
   * @param list
   * @return Sum of difference of all distinct pairs (i < j in list), list must be ordered
   */
  public static long sumOfDiffOfPairs(List<Long> list) {
    long sum = 0;
    final int size = list.size();
    for (int i = 0; i < size; i++) {
      long factor = 2L*i + 1L - size;
      sum += factor * list.get(i);
    }
    return sum;
  }

  public static long areaOfPoly(List<Vec2> path) {
    long sum = 0;
    long x1 = 0;
    long y1 = 0;

    for (Vec2 pos: path) {
      final long x2 = pos.getX();
      final long y2 = pos.getY();
      sum += x1 * y2 - x2 * y1;

      x1 = x2;
      y1 = y2;
    }

    return sum / 2;
  }

  public static <K, V> Map<K, V> patch(Map<K, V> map, K key, V value) {
    final HashMap<K, V> res = new HashMap<>(map);
    res.put(key, value);
    return res;
  }

  public static <T> T exactlyOne(Collection<T> set) {
    if (set.size() != 1) {
      throw new RuntimeException("Expected exactly one item in the set: " + set);
    }
    return set.iterator().next();
  }

  public static class GcdResult {
    public final long d;
    public final long a;
    public final long b;

    public GcdResult(long d, long a, long b) {
      this.d = d;
      this.a = a;
      this.b = b;
    }

    @Override
    public String toString() {
      return "GcdResult{" +
              "d=" + d +
              ", a=" + a +
              ", b=" + b +
              '}';
    }
  }

  public static long[] accumulate(long[] arr) {
    final long[] res = new long[arr.length];
    long running = 0;
    for (int i = 0; i < arr.length; i++) {
      running += arr[i];
      res[i] = running;
    }
    return res;
  }

  List<Long> accumulativeSum(List<Long> list) {
    long running = 0;
    final ArrayList<Long> res = new ArrayList<>();
    for (Long val : list) {
      running += val;
      res.add(running);
    }
    return res;
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
