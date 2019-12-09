package intcode;

import aoc.Util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
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

  private final String name;
  private final Memory memory;
  private final ProgramAnalysis analysis;
  private int pc;
  private State state = State.WAITING_FOR_INPUT;
  private RuntimeException exception;
  private int relativeBase = 0;

  private IntCode(String name, List<Integer> program) {
    this.name = name;
    this.memory = new Memory(program);
    this.analysis = new ProgramAnalysis(this.memory);
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

  public static IntCode fromString(String s) {
    return fromResource("code", new StringReader(s));
  }

  public static IntCode fromResource(String name) {
    return fromResource(name, Util.fromResource(name));
  }

  private static IntCode fromResource(String name, Reader input) {
    List<Integer> program = new ArrayList<>();
    try (Scanner scanner = new Scanner(input)) {
      scanner.useDelimiter(DELIMITER);
      while (scanner.hasNext()) {
        String token = scanner.next();
        program.add(Integer.parseInt(token));
      }
    }
    return new IntCode(name, program);
  }

  @Override
  public void run() {
    if (state == State.RUNNING) {
      return;
    }
    if (state == State.HALTED) {
      return;
    }
    if (state == State.CRASHED) {
      throw exception;
    }


    try {
      while (true) {
        int startPC = pc;
        OpCode opCode = OpCode.fetchOpcode(this, pc);
        this.state = opCode.execute(this);
        if (state == State.WAITING_FOR_INPUT) {
          return;
        }
        analysis.markOpCode(startPC, opCode);
        if (startPC == pc) {
          pc += opCode.size();
        } else {
          analysis.markLabel(pc);
        }
        if (state == State.HALTED) {
          return;
        }
      }
    } catch (Exception e) {
      state = State.CRASHED;
      exception = new RuntimeException(e);
      throw exception;
    }
  }

  public void printAnalysis(String filename) {
    try (PrintWriter writer = new PrintWriter(new File(filename), StandardCharsets.UTF_8)) {
      writer.println("Analysis of " + name);
      analysis.printAnalysis(writer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public int getParameter(int pc) {
    return memory.read(pc);
  }

  public int readValue(int address) {
    analysis.markRead(address);
    return memory.read(address);
  }

  public void put(int address, int value) {
    analysis.markWrite(address);
    memory.write(address, value);
  }

  public Integer pollStdin() {
    return stdin.poll();
  }

  public void jumpTo(int target) {
    pc = target;
  }

  public void writeStdout(int value) {
    stdout.add(value);
  }

  public void adjustRelativeBase(int value) {
    relativeBase += value;
  }

  public int getRelativeBase() {
    return relativeBase;
  }

  public enum State {
    RUNNING,
    WAITING_FOR_INPUT,
    HALTED,
    CRASHED
  }
}
