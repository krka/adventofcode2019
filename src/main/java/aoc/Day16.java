package aoc;

public class Day16 {
  public static String fft(String input, int phases) {
    for (int i = 0; i < phases; i++) {
      input = fftPhase(input);
    }
    return input;
  }

  private static String fftPhase(String input) {
    int length = input.length();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int patternMul = i + 1;
      sb.append(sumChar(input.substring(i), patternMul));
    }
    return sb.toString();
  }

  private static char sumChar(String s, int patternMul) {
    int pos = 0;
    int val = 0;
    int n = s.length();
    while (pos < n) {
      // pattern: 1
      for (int i = 0; i < patternMul; i++) {
        int c = s.charAt(pos) - '0';
        val += c;
        pos++;
        if (pos == n) {
          break;
        }
      }

      // pattern: 0 - so skip
      pos += patternMul;
      if (pos >= n) {
        break;
      }

      // pattern: -1
      for (int i = 0; i < patternMul; i++) {
        int c = s.charAt(pos) - '0';
        val -= c;
        pos++;
        if (pos == n) {
          break;
        }
      }

      // pattern: 0 - so skip
      pos += patternMul;
      if (pos >= n) {
        break;
      }
    }
    return (char) ('0' + Math.abs(val) % 10);
  }

  public static String fft2(String s, int phases) {
    int n = s.length();
    int repeatedLen = n * 10000;
    int skip = Integer.parseInt(s.substring(0, 7), 10);
    int remainingLen = repeatedLen - skip;
    if (skip * 2 < repeatedLen) {
      throw new RuntimeException("Can only solve for second half");
    }

    int[] mut = new int[remainingLen];

    for (int i = 0; i < remainingLen; i++) {
      mut[i] = s.charAt((i + skip) % s.length()) - '0';
    }

    // Sum of [0..i]
    int[] sums = new int[remainingLen];

    int lastIndex = remainingLen - 1;
    for (int phase = 0; phase < phases; phase++) {

      int acc = 0;
      for (int i = 0; i < remainingLen; i++) {
        acc += mut[i];
        sums[i] = acc;
      }

      for (int j = lastIndex; j >= 0; j--) {
        int prevSum = j <= 0 ? 0 : sums[j - 1];
        int value = sums[lastIndex] - prevSum;
        mut[j] = Math.abs(value) % 10;
      }
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      char c = (char) ('0' + mut[i]);
      sb.append(c);
    }
    return sb.toString();
  }
}
