package aoc2021;

public class Day21 implements Day {

  // Range: [3..9]. Index = value - 3. value = index + 3
  private static final int[] DIE_COUNTS = new int[7];
  static {
    for (int d1 = 1; d1 <= 3; d1++) {
      for (int d2 = 1; d2 <= 3; d2++) {
        for (int d3 = 1; d3 <= 3; d3++) {
          int sum = d1 + d2 + d3;
          DIE_COUNTS[sum - 3]++;
        }
      }
    }
  }

  private static final int KEY_SPACE = 2 * 10 * 10 * 22 * 22;
  private final long[] cache = new long[KEY_SPACE];

  private final int p1;
  private final int p2;

  public Day21(int p1, int p2) {
    this.p1 = p1;
    this.p2 = p2;
  }

  public long solvePart1() {
    int p1 = this.p1;
    int p2 = this.p2;
    int s1 = 0;
    int s2 = 0;
    int die = 6;
    int rolls = 0;
    while (s2 < 1000) {
      p1 += die;
      s1 += 1 + ((p1 + 9) % 10);
      if (s1 >= 1000) {
        rolls += 3;
        break;
      }
      p2 += die + 9;
      s2 += 1 + ((p2 + 9) % 10);

      die += 18;

      if (p1 > 1000) {
        p1 %= 10;
      }
      if (p2 > 1000) {
        p2 %= 10;
      }
      if (die > 1000) {
        die %= 10;
      }
      rolls += 6;
    }
    return rolls * Math.min(s1, s2);
  }

  public long solvePart2() {
    int index = solve(p1, p2, 0, 0);
    return Math.max(cache[index], cache[index + 1]);
  }

  private int solve(int p1, int p2, int s1, int s2) {
    if (s2 >= 21) {
      return -1;
    }

    int key = p1 - 1;

    key *= 10;
    key += p2 - 1;

    key *= 22;
    key += s1;

    key *= 22;
    key += s2;

    key *= 2;

    long a = cache[key];
    long b = cache[key + 1];
    if (a != 0 || b != 0) {
      return key;
    }
    long sumA = 0;
    long sumB = 0;
    for (int i = 6; i >= 0; i--) {
      int counts = DIE_COUNTS[i];
      int newP1 = p1 + i + 3;
      if (newP1 > 10) {
        newP1 -= 10;
      }
      int newS1 = s1 + newP1;
      final int index = solve(p2, newP1, s2, newS1);
      long recA = 0;
      long recB;
      if (index == -1) {
        recB = 1;
      } else {
        recA = cache[index];
        recB = cache[index + 1];
      }
      sumA += counts * recA;
      sumB += counts * recB;
    }
    cache[key] = sumB;
    cache[key + 1] = sumA;
    return key;
  }
}