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

  @Test
  public void testDecompilerDay5() {
    testDecompile("day5.in");
  }

  @Test
  public void testDecompilerDay7() {
    testDecompile("day7.in");
  }

  @Test
  public void testDecompilerDay9() {
    testDecompile("day9.in");
  }

  @Test
  public void testDecompilerDay11() {
    testDecompile("day11.in");
  }

  @Test
  public void testDecompilerDay17() {
    testDecompile("day17.in");
  }

  private void testDecompile(String resource) {
    try {
      Decompiler decompiler = new Decompiler(IntCode.readProgram(resource));
      decompiler.decompile();
      decompiler.print();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}