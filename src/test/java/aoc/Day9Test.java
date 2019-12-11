package aoc;

import intcode.IntCode;
import org.junit.Test;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Day9Test {

  @Test
  public void testSample1() {
    String input = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99";
    IntCode vm = IntCode.fromString(input);
    vm.run();
    List<BigInteger> output = vm.drainStdout();
    List<BigInteger> expected = Arrays.asList(input.split(",")).stream()
            .map(BigInteger::new)
            .collect(Collectors.toList());
    assertEquals(expected, output);
  }

  @Test
  public void testSample2() {
    String input = "1102,34915192,34915192,7,4,7,99,0";
    IntCode vm = IntCode.fromString(input);
    vm.run();
    List<BigInteger> output = vm.drainStdout();
    List<BigInteger> expected = Arrays.asList(new BigInteger("1219070632396864"));
    assertEquals(expected, output);

  }

  @Test
  public void testSample3() {
    String input = "104,1125899906842624,99";
    IntCode vm = IntCode.fromString(input);
    vm.run();
    List<BigInteger> output = vm.drainStdout();
    List<BigInteger> expected = Arrays.asList(new BigInteger("1125899906842624"));
    assertEquals(expected, output);

  }

  @Test
  public void testPart1() {
    IntCode vm = IntCode.fromResource("day9.in");
    vm.writeStdin(1);
    vm.run();
    List<BigInteger> output = vm.drainStdout();
    List<BigInteger> expected = Arrays.asList(new BigInteger("3335138414"));
    assertEquals(expected, output);
  }

  @Test
  public void testPart2() {
    IntCode vm = IntCode.fromResource("day9.in");
    vm.writeStdin(2);
    vm.run();
    List<BigInteger> output = vm.drainStdout();
    List<BigInteger> expected = Arrays.asList(new BigInteger("49122"));
    assertEquals(expected, output);

  }
}
