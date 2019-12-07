import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

public class IntCode implements Runnable {

  private static final Pattern DELIMITER = Pattern.compile("[,\n\r]+");

  private final Queue<Integer> stdout = new LinkedBlockingQueue<>();
  private final Queue<Integer> stdin = new LinkedBlockingQueue<>();

  private final int[] program;
  private int pc;
  private State state = State.WAITING_FOR_INPUT;
  private RuntimeException exception;

  private IntCode(List<Integer> program) {
    this.program = new int[program.size()];
    for (int i = 0; i < this.program.length; i++) {
      this.program[i] = program.get(i);
    }
    run();
  }

  public State getState() {
    return state;
  }

  public void writeStdin(int value) {
    stdin.add(value);
  }

  public Queue<Integer> getStdout() {
    return stdout;
  }

  public List<Integer> drainStdout() {
    ArrayList<Integer> res = new ArrayList<>();
    while (true) {
      Integer value = stdout.poll();
      if (value == null) {
        return res;
      }
      res.add(value);
    }
  }

  public static IntCode fromResource(String name) {
    return fromResource(Util.fromResource(name));
  }

  private static IntCode fromResource(Reader input) {
    List<Integer> program = new ArrayList<>();
    try (Scanner scanner = new Scanner(input)) {
      scanner.useDelimiter(DELIMITER);
      while (scanner.hasNext()) {
        String token = scanner.next();
        program.add(Integer.parseInt(token));
      }
    }
    return new IntCode(program);
  }

  @Override
  public void run() {
    if (state == State.HALTED) {
      return;
    }
    if (state == State.CRASHED) {
      throw exception;
    }

    int restorePC = 0;
    try {
      while (true) {
        restorePC = pc;
        int opcode = consume();
        int op = opcode % 100;
        ParamType firstParam = ParamType.from((opcode / 100) % 10);
        ParamType secondParam = ParamType.from((opcode / 1000) % 10);
        ParamType thirdParam = ParamType.from((opcode / 10000) % 10);
        switch (op) {
          case 1: opAdd(firstParam, secondParam, thirdParam); break;
          case 2: opMul(firstParam, secondParam, thirdParam); break;
          case 3: opInput(firstParam, secondParam, thirdParam); break;
          case 4: opOutput(firstParam, secondParam, thirdParam); break;
          case 5: jumpIfTrue(firstParam, secondParam, thirdParam); break;
          case 6: jumpIfFalse(firstParam, secondParam, thirdParam); break;
          case 7: lessThan(firstParam, secondParam, thirdParam); break;
          case 8: equals(firstParam, secondParam, thirdParam); break;
          case 99: state = State.HALTED; return;
          default: throw new RuntimeException("Unknown opcode: " + opcode + " at position " + (pc - 1));
        }
      }
    } catch (WaitForStdin e) {
      state = State.WAITING_FOR_INPUT;
      pc = restorePC;
    } catch (Exception e) {
      state = State.CRASHED;
      exception = new RuntimeException(e);
      throw exception;
    }
  }

  private void lessThan(ParamType firstParam, ParamType secondParam, ParamType thirdParam) {
    int firstArg = resolveSrc(firstParam);
    int secondArg = resolveSrc(secondParam);

    assertPosition(thirdParam);
    int res = firstArg < secondArg ? 1 : 0;
    program[consume()] = res;
  }

  private void equals(ParamType firstParam, ParamType secondParam, ParamType thirdParam) {
    int firstArg = resolveSrc(firstParam);
    int secondArg = resolveSrc(secondParam);

    assertPosition(thirdParam);
    int res = firstArg == secondArg ? 1 : 0;
    program[consume()] = res;
  }

  private void jumpIfTrue(ParamType firstParam, ParamType secondParam, ParamType thirdParam) {
    int firstArg = resolveSrc(firstParam);
    int secondArg = resolveSrc(secondParam);
    assertPosition(thirdParam);
    if (firstArg != 0) {
      pc = secondArg;
    }
  }

  private void jumpIfFalse(ParamType firstParam, ParamType secondParam, ParamType thirdParam) {
    int firstArg = resolveSrc(firstParam);
    int secondArg = resolveSrc(secondParam);
    assertPosition(thirdParam);
    if (firstArg == 0) {
      pc = secondArg;
    }
  }

  private void opAdd(ParamType firstParam, ParamType secondParam, ParamType thirdParam) {
    int firstArg = resolveSrc(firstParam);
    int secondArg = resolveSrc(secondParam);
    assertPosition(thirdParam);
    program[consume()] = firstArg + secondArg;
  }

  private void opMul(ParamType firstParam, ParamType secondParam, ParamType thirdParam) {
    int firstArg = resolveSrc(firstParam);
    int secondArg = resolveSrc(secondParam);
    assertPosition(thirdParam);
    program[consume()] = firstArg * secondArg;
  }

  private void opInput(ParamType firstParam, ParamType secondParam, ParamType thirdParam) throws WaitForStdin {
    Integer value = stdin.poll();
    if (value == null) {
      throw WaitForStdin.INSTANCE;
    }

    assertPosition(firstParam);
    assertPosition(secondParam);
    assertPosition(thirdParam);

    program[consume()] = value;
  }

  private void opOutput(ParamType firstParam, ParamType secondParam, ParamType thirdParam) {
    assertPosition(secondParam);
    assertPosition(thirdParam);

    stdout.add(resolveSrc(firstParam));
  }

  private int resolveSrc(ParamType param) {
    int value = consume();
    if (param == ParamType.POSITION) {
      return program[value];
    } else {
      // IMMEDIATE
      return value;
    }
  }

  private void assertPosition(ParamType thirdParam) {
    if (thirdParam != ParamType.POSITION) {
      throw new RuntimeException("Unexpected " + thirdParam + " at position " + (pc - 1));
    }
  }

  private int consume() {
    return program[pc++];
  }

  private enum ParamType {
    POSITION,
    IMMEDIATE;

    static ParamType from(int i) {
      return ParamType.values()[i];
    }
  }

  public enum State {
    WAITING_FOR_INPUT,
    HALTED,
    CRASHED
  }

  private static class WaitForStdin extends Exception {
    private static final WaitForStdin INSTANCE = new WaitForStdin();
  }
}
