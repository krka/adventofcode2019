package aoc2020;

import org.junit.Test;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;
import org.petitparser.tools.ExpressionBuilder;
import util.Util;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day18Test {

  public static final String DAY = "18";
  public static final String YEAR = "2020";

  public static final String MAIN_INPUT = YEAR + "/day" + DAY + ".in";
  public static final String SAMPLE_INPUT = YEAR + "/day" + DAY + "-sample.in";

  @Test
  public void testPart1() {
    assertEquals(5374004645253L, solvePart1(MAIN_INPUT));
  }

  @Test
  public void testPart2() {
    assertEquals(88782789402798L, solvePart2(MAIN_INPUT));
  }

  private long solvePart1(String name) {
    List<String> input = Util.readResource(name);

    ExpressionBuilder expressionBuilder = new ExpressionBuilder();

    expressionBuilder.group()
            .primitive(CharacterParser.digit().plus().flatten().trim().map((String s) -> new BigInteger(s)));
    expressionBuilder.group()
            .wrapper(CharacterParser.of('(').trim(), CharacterParser.of(')').trim(), (List<Object> o) -> o.get(1));
    expressionBuilder.group()
            .left(CharacterParser.of('+').trim(), (List<Object> o) -> ((BigInteger)o.get(0)).add((BigInteger)o.get(2)))
            .left(CharacterParser.of('*').trim(), (List<Object> o) -> ((BigInteger)o.get(0)).multiply((BigInteger)o.get(2)));

    Parser parser = expressionBuilder.build();

    return input.stream().mapToLong(s -> eval(parser, s)).sum();
  }

  private long eval(Parser parser, String s) {
    Result result = parser.end().parse(s);
    BigInteger o = result.get();
    return o.longValueExact();
  }


  private long solvePart2(String name) {
    List<String> input = Util.readResource(name);

    ExpressionBuilder expressionBuilder = new ExpressionBuilder();

    expressionBuilder.group().primitive(CharacterParser.digit().plus().flatten().trim().map((String s) -> new BigInteger(s)));
    expressionBuilder.group()
            .wrapper(CharacterParser.of('(').trim(), CharacterParser.of(')').trim(), (List<Object> o) -> o.get(1));
    expressionBuilder.group()
            .left(CharacterParser.of('+').trim(), (List<Object> o) -> ((BigInteger)o.get(0)).add((BigInteger)o.get(2)));
    expressionBuilder.group()
            .left(CharacterParser.of('*').trim(), (List<Object> o) -> ((BigInteger)o.get(0)).multiply((BigInteger)o.get(2)));

    Parser parser = expressionBuilder.build();

    return input.stream().mapToLong(s -> eval(parser, s)).sum();
  }

}
