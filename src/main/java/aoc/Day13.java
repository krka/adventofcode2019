package aoc;

import intcode.IntCode;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Day13 {

  private final IntCode intCode;
  private BigInteger score;
  private Map<Vector3, Integer> map = new HashMap<>();
  private int rows = 0;
  private int cols = 0;
  private Vector3 ball = Vector3.zero();
  private Vector3 ballDir = Vector3.zero();
  private Vector3 paddle = Vector3.zero();

  public Day13() {
    intCode = IntCode.fromResource("day13.in");
  }

  public BigInteger solvePart2() {
    intCode.put(BigInteger.ZERO, BigInteger.valueOf(2));
    intCode.run();
    while (intCode.getState() != IntCode.State.HALTED) {
      consumeOutput();
      if (intCode.getState() == IntCode.State.WAITING_FOR_INPUT) {
        movePaddle();
        intCode.run();
      }
    }
    consumeOutput();

    return score;
  }

  private void movePaddle() {
    long distance = paddle.y - ball.y;
    long targetX = ball.x + ballDir.x * distance;
    long paddleX = paddle.x;
    int dir = (int) Math .signum(targetX - paddleX);

    if (distance == 1) {
      //printGame(map, rows, cols);
    }
    if (distance == 1 && dir == 0) {
      // Success! Where to go next? Depends on where it's coming from
      if (ball.x < paddleX) {
        dir = -1;
      } else {
        dir = 1;
      }
    }
    intCode.writeStdin(dir);
  }

  private void consumeOutput() {
    List<BigInteger> output = intCode.drainStdout();
    Iterator<BigInteger> iterator = output.iterator();
    while (iterator.hasNext()) {
      int x = iterator.next().intValueExact();
      int y = iterator.next().intValueExact();
      BigInteger last = iterator.next();
      if (x == -1 && y == 0) {
        score = last;
      } else {
        int tile = last.intValueExact();
        Vector3 current = new Vector3(x, y, 0);
        map.put(current, tile);
        if (tile == 4) {
          Vector3 oldBall = ball;
          ball = current;
          ballDir = current.sub(oldBall);
        }
        if (tile == 3) {
          paddle = current;
        }
        rows = Math.max(rows, y);
        cols = Math.max(cols, x);
      }
    }
  }

  private void printGame(Map<Vector3, Integer> map, int rows, int cols) {
    for (int y = 0; y <= rows; y++) {
      for (int x = 0; x <= cols; x++) {
        int tile = map.getOrDefault(new Vector3(x, y, 0), 0);
        System.out.print(getTile(tile));
      }
      System.out.println();
    }
  }

  private char getTile(int tile) {
    switch (tile) {
      case 0: return ' ';
      case 1: return '#';
      case 2: return 'x';
      case 3: return '=';
      case 4: return 'o';
    }
    return '?';
  }

}
