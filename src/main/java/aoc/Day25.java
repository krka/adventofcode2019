package aoc;

import intcode.IntCode;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day25 {

  private static final Pattern PATTERN = Pattern.compile("You should be able to get in by typing (.+) on the keypad at the main airlock.");
  private final IntCode intCode;

  public Day25(String name) {
    intCode = IntCode.fromResource(name);
  }

  String part1() {
    intCode.run();
    intCode.readAllASCIILines();
    String s = "east,north,north,take spool of cat6,south,east,take mug,north,north,west,take asterisk,south,take monolith,north,east,south,east,take sand,south,west,take prime number,east,north,east,south,take tambourine,west,take festive hat,north";
    for (String s2 : s.split(",")) {
      intCode.writeASCIILine(s2);
      intCode.run();
      intCode.readAllASCIILines();
    }

    String[] drops = "drop spool of cat6,drop monolith,drop prime number,drop festive hat,drop mug,drop asterisk,drop sand,drop tambourine".split(",");
    for (int mask = 0; mask < 256; mask++) {
      IntCode fork = intCode.fork();
      for (int dropI = 0; dropI < 8; dropI++) {
        if (0 != (mask & (1 << dropI))) {
          fork.writeASCIILine(drops[dropI]);
          fork.run();
          fork.drainStdout();
        }
      }

      fork.writeASCIILine("west");
      fork.run();

      Optional<String> code = fork.readAllASCIILines().stream()
              .map(PATTERN::matcher)
              .filter(Matcher::find)
              .map(matcher -> matcher.group(1))
              .findAny();
      if (code.isPresent()) {
        return code.get();
      }
    }

    throw new RuntimeException("No solution");
  }
}
