package intcode;

import intcode.assembler.Assembler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class FizzBuzzTest {

  private IntCode intCode;

  @Before
  public void setUp() {
    intCode = IntCode.fromResource(Assembler.compile("fizzbuzz.asm"));
    intCode.run();
  }

  @Test
  public void testSimple() {
    intCode.writeStdin(20);
    intCode.run();
    List<String> lines = intCode.readAllASCIILines();
    lines.forEach(System.out::println);
  }
}
