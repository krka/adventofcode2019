package intcode;

import intcode.assembler.AnnotatedIntCode;
import intcode.assembler.Assembler;
import org.junit.Test;

import java.util.List;

public class FizzBuzzTest {

  @Test
  public void testPart1() {
    AnnotatedIntCode annotatedIntCode = Assembler.compileAnnotated("fizzbuzz.asm");
    System.out.println(annotatedIntCode.toString());
    System.out.println(annotatedIntCode.toProgram());
    IntCode intCode = IntCode.fromResource(annotatedIntCode.getIntCode());
    intCode.run();

    intCode.writeStdin(20);
    intCode.run();
    List<String> lines = intCode.readAllASCIILines();
    lines.forEach(System.out::println);
  }

  @Test
  public void testPart2() {
    IntCode intCode = IntCode.fromResource(Assembler.compile("fizzbuzz2.asm"));
    intCode.run();

    intCode.writeStdin(200000);
    intCode.writeStdin(20);
    intCode.run();
    List<String> lines = intCode.readAllASCIILines();
    lines.forEach(System.out::println);
  }
}
