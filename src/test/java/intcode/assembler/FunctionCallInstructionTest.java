package intcode.assembler;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.*;

public class FunctionCallInstructionTest {
  @Test
  public void testSimple() {
    FunctionCallInstruction functionCallInstruction = new FunctionCallInstruction();
    Matcher matcher = functionCallInstruction.applyMatch("z, w = swap(x, y)");
    assertTrue(matcher.matches());
    assertEquals("swap", matcher.group("functionname"));
    assertEquals("x, y", matcher.group("parameters"));
    assertEquals("z, w", matcher.group("returnvalues"));
  }
}