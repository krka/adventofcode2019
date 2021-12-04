package aoc2021;

import util.Util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day4 {

  private final List<String> input;
  private final List<Integer> numbers;
  private final List<Integer> boards;
  private final int numBoards;

  public Day4(String name) {
    input = Util.readResource(name).stream()
            .collect(Collectors.toList());

    numbers = Arrays.stream(input.get(0).split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

    boards = input.subList(1, input.size()).stream()
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .map(String::strip)
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .collect(Collectors.toList());

    numBoards = boards.size() / 25;
  }

  public long solvePart1() {
    Set<Integer> marked = new HashSet<>();
    for (Integer number : numbers) {
      marked.add(number);
      for (int board = 0; board < numBoards; board++) {
        if (checkWin(marked, board)) {
          return countBoard(marked, board) * number;
        }
      }
    }
    return -1;
  }

  public long solvePart2() {
    Set<Integer> marked = new HashSet<>();
    Set<Integer> complete = new HashSet<>();
    int remaining = numBoards;
    for (Integer number : numbers) {
      marked.add(number);
      for (int board = 0; board < numBoards; board++) {
        if (!complete.contains(board)) {
          if (checkWin(marked, board)) {
            complete.add(board);
            remaining--;
            if (remaining == 0) {
              return countBoard(marked, board) * number;
            }
          }
        }
      }
    }
    return -1;
  }

  private long countBoard(Set<Integer> marked, int board) {
    int start = board * 25;
    int unmarkedSum = 0;
    for (int i = 0; i < 25; i++) {
      int num = boards.get(start + i);
      if (!marked.contains(num)) {
        unmarkedSum += num;
      }
    }
    return unmarkedSum;
  }

  private boolean checkWin(Set<Integer> marked, int board) {
    if (checkLines(marked, board, 1, 5)) {
      return true;
    }
    if (checkLines(marked, board, 5, 1)) {
      return true;
    }
    return false;
  }

  private boolean checkLines(Set<Integer> marked, int board, int linesStep, int timesStep) {
    for (int i = 0; i < 5; i++) {
      if (checkLine(marked, board, i * timesStep, linesStep)) {
        return true;
      }
    }
    return false;
  }

  private boolean checkLine(Set<Integer> marked, int board, int start, int step) {
    start += board * 25;
    for (int i = 0; i < 5; i++) {
      if (!marked.contains(boards.get(start + i * step))) {
        return false;
      }
    }
    return true;
  }

}
