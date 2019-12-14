package util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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

  public static BigInteger plus1(BigInteger x) {
    return x.add(BigInteger.ONE);
  }
  public static BigInteger plus2(BigInteger x) {
    return x.add(BigInteger.TWO);
  }
  public static BigInteger plus3(BigInteger x) {
    return x.add(THREE);
  }

  public static List<BigInteger> toBigInt(List<Integer> list) {
    return list.stream().map(BigInteger::valueOf).collect(Collectors.toList());
  }
}
