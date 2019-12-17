package aoc;

import intcode.IntCode;

import java.math.BigInteger;
import java.util.List;

public class Day17 {

  private final IntCode intcode;

  public Day17(String name) {
    intcode = IntCode.fromResource(name);
  }

  public int part1() {
    intcode.run();
    String s = IntCode.asASCII(intcode.drainStdout());
    String[] lines = s.split("\n");
    int height = lines.length;
    int width = lines[0].length();

    int sum = 0;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        char c = get(lines, row, col);
        if (c == '#'
                && get(lines, row - 1, col) == '#'
                && get(lines, row + 1, col) == '#'
                && get(lines, row, col - 1) == '#'
                && get(lines, row, col + 1) == '#'
        ) {
          sum += row * col;
        }
      }
    }
    System.out.println(s);

    return sum;
  }

  private char get(String[] lines, int row, int col) {
    if (row < 0 || row >= lines.length) {
      return '.';
    }
    String line = lines[row];
    if (col < 0 || col >= line.length()) {
      return '.';
    }
    return line.charAt(col);
  }

  public int part2() {
    // Start:
    // R,8,L,10,L,12,R,4,

    // End:
    // R,8,L,12,R,4,R,4

    // Middle:
    // R,8,R,8,L,10,L,12,R,4

    // Path:
    // R 8, L 10, L 12, R 4, R 8
    // L 12, R 4, R 4,
    // R 8, L 10, L 12, R 4, R 8,
    // L 10, R 8,
    // R 8, L 10, L 12, R 4, R 8,
    // L 12, R 4, R 4,
    // R 8, L 10, R 8, R 8,
    // L 12, R 4, R 4,
    // R 8, L 10, R 8,

    // Fixed:
    // R 8, L 12, R 4, R 4

    // Both start and middle:
    String a = "R,8,L,10,L,12,R,4";

    String b = "R,8,L,10,R,8";

    // Fixed
    String c = "R,8,L,12,R,4,R,4";

    String main = "A,C,A,B,A,C,B,C,B,C";

    intcode.readAllASCIILines().forEach(System.out::println);

    intcode.put(BigInteger.ZERO, BigInteger.TWO);
    intcode.run();

    intcode.readAllASCIILines().forEach(System.out::println);
    writeAndPrint(main);
    intcode.run();

    intcode.readAllASCIILines().forEach(System.out::println);
    writeAndPrint(a);
    intcode.run();

    intcode.readAllASCIILines().forEach(System.out::println);
    writeAndPrint(b);
    intcode.run();

    intcode.readAllASCIILines().forEach(System.out::println);
    writeAndPrint(c);
    intcode.run();

    intcode.readAllASCIILines().forEach(System.out::println);
    writeAndPrint("n");
    intcode.run();

    readUntilBlankLine();
    readUntilBlankLine();

    List<BigInteger> ans = intcode.drainStdout();
    if (ans.size() != 1) {
      throw new RuntimeException("Unexpected output: " + ans);
    }
    return ans.get(0).intValueExact();
  }

  private void readUntilBlankLine() {
    while (true) {
      String x = intcode.readASCIILine();
      if (x.isEmpty()) {
        break;
      }
      System.out.println(x);
    }
  }

  private void writeAndPrint(String s) {
    System.out.println("> " + s);
    intcode.writeASCIILine(s);
  }
}
