package aoc2019;

import intcode.IntCode;
import util.Util;

import java.math.BigInteger;
import java.util.List;

public class Day21 {

  private final String name;

  public Day21(String name) {
    this.name = name;
  }

  public int part1() {
    IntCode intCode = IntCode.fromResource(name);

    String program = "NOT A J\n" +
            "NOT B T\n" +
            "OR T J\n" +
            "NOT C T\n" +
            "OR T J\n" +
            "AND D J";
    intCode.writeASCIILine(program);
    intCode.writeASCIILine("WALK");
    intCode.run();
    intCode.readAllASCIILines().forEach(System.out::println);

    List<BigInteger> output = intCode.drainStdout();
    if (output.size() == 1) {
      return output.get(0).intValue();
    }
    throw new RuntimeException(output.toString());
  }

  public int part2() {
    IntCode intCode = IntCode.fromResource(name);

    List<String> program = Util.readResource("2019/day21-springcode-part2.txt");
    for (String line : program) {
      if (!line.isEmpty() && !line.startsWith("#")) {
        intCode.writeASCIILine(line);
      }
    }
    intCode.writeASCIILine("RUN");

    intCode.run();
    intCode.readAllASCIILines().forEach(System.out::println);

    List<BigInteger> output = intCode.drainStdout();
    if (output.size() == 1) {
      return output.get(0).intValue();
    }
    throw new RuntimeException(output.toString());
  }


}
