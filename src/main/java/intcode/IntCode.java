package intcode;

import util.Util;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IntCode implements Runnable {

  private static final Pattern DELIMITER = Pattern.compile("[,\n\r]+");

  private final Queue<BigInteger> stdout = new LinkedBlockingQueue<>();
  private final Queue<BigInteger> stdin = new LinkedBlockingQueue<>();

  private final Memory memory;
  private BigInteger pc = BigInteger.ZERO;
  private State state = State.NOT_STARTED;
  private RuntimeException exception;
  private BigInteger relativeBase = BigInteger.ZERO;
  private Consumer<String> debugger = s -> {};

  private IntCode(List<BigInteger> program) {
    this.memory = new Memory(program);
  }

  public static String asASCII(List<BigInteger> output) {
    return output.stream().mapToInt(BigInteger::intValueExact)
            .mapToObj(Character::toString)
            .collect(Collectors.joining());
  }

  public void setDebugger(boolean enabled) {
    if (enabled) {
      debugger = s -> System.out.printf("%05d: %s%n", pc, s);
    } else {
      debugger = s -> {};
    }
  }

  public State getState() {
    return state;
  }

  public void writeStdin(BigInteger value) {
    stdin.add(value);
  }

  public void writeStdin(int value) {
    writeStdin(BigInteger.valueOf(value));
  }

  public Queue<BigInteger> getStdout() {
    return stdout;
  }

  public List<BigInteger> drainStdout() {
    ArrayList<BigInteger> res = new ArrayList<>();
    while (true) {
      BigInteger value = stdout.poll();
      if (value == null) {
        return res;
      }
      res.add(value);
    }
  }

  public static IntCode fromString(String s) {
    return fromResource(new StringReader(s));
  }

  public static IntCode fromResource(String name) {
    return new IntCode(readProgram(name));
  }

  public static IntCode fromResource(Reader input) {
    return new IntCode(readProgram(input));
  }

  public static List<BigInteger> readProgram(String name) {
    return readProgram(Util.fromResource(name));
  }

  static List<BigInteger> readProgram(Reader input) {
    List<BigInteger> program = new ArrayList<>();
    try (Scanner scanner = new Scanner(input)) {
      scanner.useDelimiter(DELIMITER);
      while (scanner.hasNext()) {
        String token = scanner.next();
        program.add(new BigInteger(token));
      }
    }
    return program;
  }

  @Override
  public void run() {
    if (state == State.HALTED) {
      return;
    }
    if (state == State.CRASHED) {
      throw exception;
    }

    if (state != State.WAITING_FOR_INPUT && state != State.NOT_STARTED) {
      throw new RuntimeException("Unexpected state: " + state);
    }
    state = State.RUNNING;

    try {
      while (true) {
        OpCode opcode = OpCode.decode(memory, pc);
        try {
          opcode.execute(this, debugger);
          pc = pc.add(BigInteger.valueOf(opcode.size()));
        } catch (JumpTo jump) {
          pc = jump.getAddress();
        } catch (WaitForInput e) {
          state = State.WAITING_FOR_INPUT;
          return;
        } catch (OpCode.Halt e) {
          state = State.HALTED;
          return;
        }
      }
    } catch (Exception e) {
      state = State.CRASHED;
      exception = new RuntimeException(e);
      throw exception;
    }
  }

  public BigInteger readValue(BigInteger address) {
    return memory.read(address);
  }

  public void put(BigInteger address, BigInteger value) {
    memory.write(address, value);
  }

  public BigInteger pollStdin() {
    return stdin.poll();
  }

  public void writeStdout(BigInteger value) {
    stdout.add(value);
  }

  public void adjustRelativeBase(BigInteger value) {
    relativeBase = relativeBase.add(value);
  }

  public BigInteger getRelativeBase() {
    return relativeBase;
  }

  public void writeASCIILine(String s) {
    s.chars().forEach(this::writeStdin);
    writeStdin(10);
  }

  public String readASCIILine() {
    if (stdout.isEmpty()) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    while (true) {
      BigInteger value = stdout.poll();
      if (value == null) {
        throw new RuntimeException("Unfinished ascii line: '" + sb.toString() + "'");
      }
      if (value.intValueExact() == 10) {
        return sb.toString();
      } else {
        sb.append((char) (value.intValueExact()));
      }
    }
  }

  public List<String> readAllASCIILines() {
    ArrayList<String> res = new ArrayList<>();
    while (true) {
      String s = readASCIILine();
      if (s == null) {
        return res;
      } else {
        res.add(s);
      }
    }
  }

  public enum State {
    NOT_STARTED,
    RUNNING,
    WAITING_FOR_INPUT,
    HALTED,
    CRASHED
  }
}
