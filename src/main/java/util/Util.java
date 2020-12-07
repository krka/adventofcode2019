package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

  public static <T> Collector<T, ?, List<List<T>>> toPartitions(BiFunction<T, T, Boolean> predicate) {
    return new Partitions<>(predicate);
  }

  public static <T> Collector<T, ?, List<List<T>>> splitBy(Predicate<T> predicate) {
    return new SplitStream<>(predicate);
  }

  public static <T> AdjCollector<T> adj() {
    return new AdjCollector<>();
  }

  public static IntBinaryOperator exactlyOne() throws RuntimeException {
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
}
