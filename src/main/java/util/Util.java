package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
  private static final ClassLoader CLASS_LOADER = Util.class.getClassLoader();
  private static final BigInteger THREE = BigInteger.valueOf(3);

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
}
