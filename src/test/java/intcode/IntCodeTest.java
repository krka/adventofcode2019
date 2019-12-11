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
}