import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class Util {
  private static final ClassLoader CLASS_LOADER = Util.class.getClassLoader();

  static Reader fromResource(String name) {
    InputStream stream = CLASS_LOADER.getResourceAsStream(name);
    if (stream == null) {
      try {
        return new InputStreamReader(new FileInputStream("src/test/resources/" + name), StandardCharsets.UTF_8);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
      //throw new IllegalArgumentException("Resource not found: " + name);
    }
    return new InputStreamReader(stream, StandardCharsets.UTF_8);
  }
}
