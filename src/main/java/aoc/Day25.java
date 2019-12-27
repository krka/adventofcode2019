package aoc;

import intcode.IntCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day25 {

  private static final Pattern ROOM_NAME_PATTERN = Pattern.compile("== (.+) ==");
  private static final Pattern CANT_MOVE_PATTERN = Pattern.compile("You can't move!!");
  private static final Pattern WIN_PATTERN = Pattern.compile("You should be able to get in by typing (.+) on the keypad at the main airlock.");
  private IntCode intCode;

  public Day25(String name) {
    intCode = IntCode.fromResource(name);
  }

  String invert(String dir) {
    switch (dir) {
      case "east": return "west";
      case "west": return "east";
      case "north": return "south";
      case "south": return "north";
    }
    throw new RuntimeException("Unknown direction: " + dir);
  }

  String part1() {
    Map<String, List<String>> visited = new HashMap<>();
    List<String> currentPath = new ArrayList<>();
    List<String> items = new ArrayList<>();

    intCode.run();
    ParseResult parseResult = parse(intCode.readAllASCIILines());

    dfs(parseResult, currentPath, visited, items);

    visited.forEach((s, strings) -> System.out.println(s + " -> " + strings));
    System.out.println("Items found: " + items);

    List<String> path = visited.get("Security Checkpoint");
    String skipDir = invert(path.get(path.size() - 1));

    ParseResult finalResult = movePath(path);
    List<String> dirs = finalResult.directions.stream().filter(s -> !s.equals(skipDir)).collect(Collectors.toList());
    if (dirs.size() != 1) {
      throw new RuntimeException("Too many dirs in last room: " + dirs);
    }
    String finalDir = dirs.get(0);

    int size = items.size();
    int combinations = 1 << size;
    for (int mask = 0; mask < combinations; mask++) {
      IntCode fork = intCode.fork();
      Set<String> dropped = new HashSet<>();
      for (int dropI = 0; dropI < size; dropI++) {
        if (0 != (mask & (1 << dropI))) {
          String item = items.get(dropI);
          dropped.add(item);
          fork.writeASCIILine("drop " + item);
          fork.run();
          fork.drainStdout();
        }
      }

      fork.writeASCIILine(finalDir);
      fork.run();

      Optional<String> code = fork.readAllASCIILines().stream()
              .map(WIN_PATTERN::matcher)
              .filter(Matcher::find)
              .map(matcher -> matcher.group(1))
              .findAny();
      if (code.isPresent()) {
        System.out.println("Solution uses items: " + items.stream().filter(item -> !dropped.contains(item)).collect(Collectors.joining(", ")));
        return code.get();
      }
    }

    throw new RuntimeException("No solution");
  }

  private ParseResult movePath(List<String> path) {
    ParseResult parseResult = null;
    for (String s : path) {
      parseResult = move(s);
    }
    return parseResult;
  }

  private void dfs(ParseResult parseResult, List<String> path, Map<String, List<String>> visited, List<String> items) {
    if (visited.containsKey(parseResult.roomName)) {
      return;
    }

    visited.put(parseResult.roomName, new ArrayList<>(path));

    System.out.println("Room: " + parseResult.roomName);
    System.out.println(parseResult.directions);
    System.out.println(parseResult.items);
    System.out.println();


    IntCode backup = intCode.fork();
    for (String item : parseResult.items) {
      intCode.writeASCIILine("take " + item);
      intCode.step(10000);
      if (intCode.getState() == IntCode.State.HALTED) {
        intCode = backup;
        System.out.println("Item " + item + " kills you!");
      } else if (intCode.getState() == IntCode.State.PAUSED) {
        intCode = backup;
        System.out.println("Item " + item + " gets you stuck in an infinite loop!");
      } else if (!canMove(parseResult)) {
        intCode = backup;
        System.out.println("Item " + item + " makes you stuck!");
      } else {
        System.out.println("Took item " + item);
        intCode.run();
        items.add(item);
      }
    }

    if (parseResult.roomName.equals("Security Checkpoint")) {
      return;
    }

    for (String direction : parseResult.directions) {
      path.add(direction);
      ParseResult parseResult2 = move(direction);
      dfs(parseResult2, path, visited, items);
      ParseResult parseResult3 = move(invert(direction));
      if (!parseResult3.roomName.equals(parseResult.roomName)) {
        throw new RuntimeException("could not move back");
      }
      path.remove(path.size() - 1);
    }
  }

  private ParseResult move(String direction) {
    intCode.writeASCIILine(direction);
    intCode.run();
    return parse(intCode.readAllASCIILines());
  }

  private boolean canMove(ParseResult from) {
    String dir = from.directions.get(0);
    ParseResult parseResult = move(dir);
    if (parseResult.roomName.equals("CANT MOVE")) {
      return false;
    } else {
      ParseResult parseResult2 = move(invert(dir));
      if (parseResult2.roomName.equals(from.roomName)) {
        return true;
      } else {
        throw new RuntimeException("Could not move back");
      }
    }
  }

  private ParseResult parse(List<String> lines) {
    List<String> directions = new ArrayList<>();
    List<String> items = new ArrayList<>();
    String roomName = "<unknown room>";

    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      Matcher matcher = ROOM_NAME_PATTERN.matcher(line);
      if (matcher.find()) {
        roomName = matcher.group(1);
      } else if (CANT_MOVE_PATTERN.matcher(line).find()) {
        return new ParseResult("CANT MOVE", Collections.emptyList(), Collections.emptyList());
      } else if (line.equals("Doors here lead:")) {
          i = add(lines, i, directions);
      } else if (line.equals("Items here:")) {
          i = add(lines, i, items);
      }
    }
    if (roomName.equals("<unknown room>")) {
      throw new RuntimeException("Unknown room name");
    }
    return new ParseResult(roomName, directions, items);
  }

  private int add(List<String> lines, int i, List<String> res) {
    while (true) {
      i++;
      String line = lines.get(i);
      if (line.isEmpty()) {
        return i;
      }
      res.add(line.substring(2));
    }
  }

  private class ParseResult {
    private final String roomName;
    private final List<String> directions;
    private final List<String> items;

    public ParseResult(String roomName, List<String> directions, List<String> items) {
      this.roomName = roomName;
      this.directions = directions;
      this.items = items;
    }
  }
}
