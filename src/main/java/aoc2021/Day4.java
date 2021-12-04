package aoc2021;

import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Day4 {

  private final int numBoards;
  private final TreeMap<Integer, Integer> boardScores;

  public Day4(String name) {
    List<String> input = Util.readResource(name).stream()
            .collect(Collectors.toList());

    List<Integer> numbers = Arrays.stream(input.get(0).split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    Map<Integer, Integer> numberIndex = new HashMap<>();
    for (int round = 0; round < numbers.size(); round++) {
      numberIndex.put(numbers.get(round), round + 1);
    }

    List<Integer> boards = input.subList(1, input.size()).stream()
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .map(String::strip)
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .collect(Collectors.toList());

    numBoards = boards.size() / 25;

    boardScores = new TreeMap<>();

    List<Integer> cols = new ArrayList<>(5);
    for (int board = 0; board < numBoards; board++) {
      cols.clear();
      for (int i = 0; i < 5; i++) {
        cols.add(0);
      }

      int bestRow = Integer.MAX_VALUE;

      for (int row = 0; row < 5; row++) {
        int curRow = 0;
        for (int col = 0; col < 5; col++) {
          int cell = numberIndex.get(boards.get(board * 25 + row * 5 + col));
          cols.set(col, Math.max(cols.get(col), cell));
          curRow = Math.max(curRow, cell);
        }
        bestRow = Math.min(bestRow, curRow);
      }
      cols.add(bestRow);
      int minRound = cols.stream().mapToInt(Integer::intValue).min().getAsInt();
      int score = boards.subList(board * 25, board * 25 + 25).stream()
              .mapToInt(Integer::intValue)
              .filter(cell -> numberIndex.get(cell) > minRound)
              .sum();

      boardScores.put(minRound, score * numbers.get(minRound - 1));
    }
  }

  public long solvePart1() {
    return boardScores.firstEntry().getValue();
  }

  public long solvePart2() {
    return boardScores.lastEntry().getValue();
  }

}
