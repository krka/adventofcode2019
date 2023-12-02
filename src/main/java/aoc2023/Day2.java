package aoc2023;

import util.Day;
import util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class Day2 implements Day {

  private final List<Game> games;

  public Day2(String name) {
    games = Util.readResource(name).stream()
            .map(Game::new)
            .collect(Collectors.toList());
  }

  @Override
  public long solvePart1() {
    return games.stream()
            .filter(game -> game.maxRed <= 12 && game.maxGreen <= 13 && game.maxBlue <= 14)
            .mapToInt(game -> game.id).sum();
  }

  @Override
  public long solvePart2() {
    return games.stream()
            .mapToInt(game -> game.maxBlue * game.maxRed * game.maxGreen)
            .sum();
  }

  static class Game {
    private final int id;
    private final int maxBlue;
    private final int maxRed;
    private final int maxGreen;

    public Game(String line) {
      final String[] parts = line.split(":");
      id = Integer.parseInt(parts[0].trim().split(" ")[1]);
      String[] sets = parts[1].trim().split("[;,]");
      int maxBlue = 0;
      int maxRed = 0;
      int maxGreen = 0;

      for (String set : sets) {
        final String[] s = set.trim().split(" ");
        int val = Integer.parseInt(s[0]);
        switch (s[1]) {
          case "red": maxRed = Math.max(maxRed, val); break;
          case "green": maxGreen = Math.max(maxGreen, val); break;
          case "blue": maxBlue = Math.max(maxBlue, val); break;
          default: throw new RuntimeException(set);
        }
      }
      this.maxBlue = maxBlue;
      this.maxRed = maxRed;
      this.maxGreen = maxGreen;
    }
  }
}
