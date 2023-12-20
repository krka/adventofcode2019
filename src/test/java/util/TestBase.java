package util;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class TestBase {
  public static Day create(String resource) {
    final String callingClass = findCaller();
    final String className = Util.removeSuffix("Test", callingClass);
    try {
      final Class<?> clazz = TestBase.class.getClassLoader().loadClass(className);
      final String convertedResource = convertResource(resource, clazz);
      return (Day) clazz.getDeclaredConstructor(String.class).newInstance(convertedResource);
    } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private static String findCaller() {
    final RuntimeException runtimeException = new RuntimeException();
    for (StackTraceElement stackTraceElement : runtimeException.getStackTrace()) {
      if (stackTraceElement.getClassName().endsWith("Test")) {
        return stackTraceElement.getClassName();
      }
    }
    throw new RuntimeException("Could not find caller");
  }

  private static String convertResource(String name, Class<?> clazz) {
    final String dayValue = clazz.getSimpleName().toLowerCase(Locale.ROOT);
    final String yearValue = Util.removePrefix("aoc", clazz.getPackageName());
    final String replaced = name.replace("$DAY", dayValue)
            .replace("$YEAR", yearValue);
    if (replaced.contains("$DAY") ||  replaced.contains("$YEAR")) {
      throw new RuntimeException("Expected resource with $DAY and $YEAR but got: " + name);
    }
    return replaced;
  }

}
