package intcode;

import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class IntCodeTest {
  @Test
  public void testQuine() {
    String input = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99";
    IntCode intCode = IntCode.fromString(input);
    intCode.setDebugger(true);
    intCode.run();
    String output = intCode.drainStdout().stream().map(Object::toString).collect(Collectors.joining(","));
    assertEquals(input, output);
  }

  @Test(expected = RuntimeException.class)
  public void testDecompilerDay5() {
    Decompiler decompiler = new Decompiler(IntCode.readProgram("day5.in"));
    decompiler.decompile();
    decompiler.print();
  }

  @Test(expected = RuntimeException.class)
  public void testDecompilerDay7() {
    Decompiler decompiler = new Decompiler(IntCode.readProgram("day7.in"));
    decompiler.decompile();
    decompiler.print();
  }

  @Test(expected = RuntimeException.class)
  public void testDecompilerDay9() {
    Decompiler decompiler = new Decompiler(IntCode.readProgram("day9.in"));
    decompiler.decompile();
    decompiler.print();
  }

  @Test(expected = RuntimeException.class)
  public void testDecompilerDay11() {
    Decompiler decompiler = new Decompiler(IntCode.readProgram("day11.in"));
    decompiler.decompile();
    decompiler.print();
  }

}