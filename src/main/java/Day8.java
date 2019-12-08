import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day8 {

  private final List<String> layers;
  private final int width;
  private final int height;

  public Day8(String name, int width, int height) throws IOException {
    this.width = width;
    this.height = height;
    try (BufferedReader bufferedReader = new BufferedReader(Util.fromResource(name))) {
      String line = bufferedReader.readLine();
      int numPixels = width * height;

      int numLayers = line.length() / numPixels;
      if (numLayers * numPixels != line.length()) {
        throw new RuntimeException("Line has wrong length");
      }
      layers = IntStream.range(0, numLayers)
              .map(i -> i * numPixels)
              .mapToObj(i -> line.substring(i, i + numPixels)).collect(Collectors.toList());
    }
  }

  public int part1() {
    return layers.stream()
            .map(s -> s.chars().boxed().collect(Collectors.groupingBy(Function.identity())))
            .min(Comparator.comparingInt(value -> getSize(value, '0')))
            .map(map -> getSize(map, '1') * getSize(map, '2'))
            .get();
  }

  private static int getSize(Map<Integer, List<Integer>> value, char c) {
    return value.getOrDefault((int) c, Collections.emptyList()).size();
  }

  public String part2() {
    StringBuffer sb = new StringBuffer();
    for (String layer : layers) {
      char[] chars = layer.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        char newChar = chars[i];
        if (sb.length() <= i) {
          sb.append(newChar);
        } else {
          char previous = sb.charAt(i);
          if (previous == '2') {
            sb.setCharAt(i, newChar);
          }
        }
      }
    }

    for (int i = 0; i < sb.length(); i++) {
      if (sb.charAt(i) == '0') {
        sb.setCharAt(i, ' ');
      }
    }
    int insertPos = sb.length();
    for (int i = 0; i < height; i++) {
      sb.insert(insertPos, '\n');
      insertPos -= width;
    }
    return sb.toString();
  }
}
