package aoc2020;

import util.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day8 {
  private final List<String> input;

  public Day8(String name) {
    input = Util.readResource(name);
  }

  public long solvePart1() {
    int pc = 0;
    int acc = 0;
    Set<Integer> visited = new HashSet<>();
    while (true) {
      if (!visited.add(pc)) {
        break;
      }
      String s = input.get(pc);
      String[] parts = s.split(" ");
      int val = Integer.parseInt(parts[1]);
      switch (parts[0]) {
        case "acc": acc += val; pc++; break;
        case "jmp": pc += val; break;
        case "nop": pc++; break;
        default: throw new RuntimeException();
      }
    }
    return acc;
  }

  public long solvePart2() {
    long best = -1;
    for (int i = 0; i < input.size(); i++) {
      best = Math.max(best, exec(i));
    }
    return best;
  }

  private long exec(int flip) {
    int pc = 0;
    int acc = 0;
    Set<Integer> visited = new HashSet<>();
    while (true) {
      if (pc == input.size()) {
        return acc;
      }
      if (!visited.add(pc)) {
        return -1;
      }
      String s = input.get(pc);
      String[] parts = s.split(" ");
      int val = Integer.parseInt(parts[1]);
      String part = parts[0];
      if (pc == flip) {
        if (part.equals("jmp")) {
          part = "nop";
        } else if (part.equals("nop")) {
          part = "jmp";
        }
      }
      switch (part) {
        case "acc": acc += val; pc++; break;
        case "jmp": pc += val; break;
        case "nop": pc++; break;
        default: throw new RuntimeException();
      }
    }
  }
}
