package aoc;

import intcode.IntCode;
import org.junit.Test;

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
    List<Integer> output = vm.drainStdout();
    List<Integer> expected = Arrays.asList(input.split(",")).stream()
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    assertEquals(expected, output);

  }
}
