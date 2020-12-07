package intcode;

import org.junit.Test;
import util.Util;

import static org.junit.Assert.*;

public class IntCodeTest {
  @Test
  public void testQuine() {
    String input = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99";
    IntCode intCode = IntCode.fromString(input);
    intCode.run();
    String output = Util.toString(intCode.drainStdout());
    assertEquals(input, output);
  }

  @Test
  public void testDecompilerDay5() {
    testDecompile("2019/day5.in");
  }

  @Test
  public void testDecompilerDay7() {
    testDecompile("2019/day7.in");
  }

  @Test
  public void testDecompilerDay9() {
    testDecompile("2019/day9.in");
  }

  @Test
  public void testDecompilerDay11() {
    testDecompile("2019/day11.in");
  }

  @Test
  public void testDecompilerDay17() {
    testDecompile("2019/day17.in");
  }

  @Test
  public void testDecompilerDay25() {
    testDecompile("2019/day25.in");
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