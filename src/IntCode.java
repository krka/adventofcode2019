import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntCode {

  private static final Pattern DELIMITER = Pattern.compile("[,\n]+");

  private final Consumer<Integer> stdout;
  private final Callable<Integer> stdin;
  private final int[] program;
  private int pc;

  private IntCode(Consumer<Integer> stdout, Callable<Integer> stdin, List<Integer> program) {
    this.stdout = stdout;
    this.stdin = stdin;
    this.program = new int[program.size()];
    for (int i = 0; i < this.program.length; i++) {
      this.program[i] = program.get(i);
    }
  }

  public static IntCode fromFile(String filename, Consumer<Integer> stdout, Callable<Integer> stdin) {
    return fromFile(new File(filename), stdout, stdin);
  }

  private static IntCode fromFile(File file, Consumer<Integer> stdout, Callable<Integer> stdin) {
    try {
      List<Integer> program = new ArrayList<>();
      try (Scanner scanner = new Scanner(new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII))) {
        scanner.useDelimiter(DELIMITER);
        while (scanner.hasNext()) {
          String token = scanner.next();
          program.add(Integer.parseInt(token));
        }
      }
      return new IntCode(stdout, stdin, program);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void run() {
    try {
      while (true) {
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
          case 99: return;
          default: throw new RuntimeException("Unknown opcode: " + opcode + " at position " + (pc - 1));
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
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

  private void opInput(ParamType firstParam, ParamType secondParam, ParamType thirdParam) throws Exception {
    assertPosition(firstParam);
    assertPosition(secondParam);
    assertPosition(thirdParam);
    Integer value = stdin.call();

    program[consume()] = value;
  }

  private void opOutput(ParamType firstParam, ParamType secondParam, ParamType thirdParam) {
    assertPosition(secondParam);
    assertPosition(thirdParam);

    stdout.accept(resolveSrc(firstParam));
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

  public static class ListStdin implements Callable<Integer> {
    private final Queue<Integer> list = new ArrayDeque<>();

    public ListStdin(Collection<Integer> data) {
      list.addAll(data);
    }

    public static ListStdin of(int... values) {
      return new ListStdin(IntStream.of(values).boxed().collect(Collectors.toList()));
    }

    @Override
    public Integer call() throws Exception {
      return list.remove();
    }
  }

  public static class ListStdout implements Consumer<Integer> {
    private final List<Integer> list = new ArrayList<>();

    public ListStdout() {
    }

    @Override
    public void accept(Integer integer) {
      list.add(integer);
    }

    public List<Integer> getList() {
      return list;
    }
  }
}
