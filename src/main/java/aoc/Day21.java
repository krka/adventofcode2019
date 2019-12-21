package aoc;

import intcode.IntCode;

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
    return 0;
  }


}
