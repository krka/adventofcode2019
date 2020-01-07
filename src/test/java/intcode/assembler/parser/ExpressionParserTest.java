package intcode.assembler.parser;

import NegNode.NegNode;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExpressionParserTest {

  @Test
  public void testParser() {
    ExprNode expr = ExpressionParser.parse("10 * 123 + -456 - x");
    MulNode muls = new MulNode(new IntConstant(BigInteger.valueOf(10)), new IntConstant(BigInteger.valueOf(123)));
    ExprNode expected = new AddNode(new AddNode(muls, new NegNode(new IntConstant(BigInteger.valueOf(456)))), new NegNode(new VarNode("x")));
    assertEquals(expected, expr);
  }

  @Test
  public void testOptimize() {
    ExprNode expr = ExpressionParser.parse("10 * 123 + -456 - x").optimize();
    ExprNode expected = ExpressionParser.parse("774 - x").optimize();
    assertEquals(expected, expr);
  }

  @Test
  public void testSetStatement() {
    SetStatement setStatement = ExpressionParser.parseSetStatement("x = y + 3 + 5");
    assertEquals("x = y + 3 + 5", setStatement.toString());
  }

  @Test
  public void testParen() {
    ExprNode expr = ExpressionParser.parse("10 * (123 + 5)").optimize();
    ExprNode expected = ExpressionParser.parse("1280").optimize();
    assertEquals(expected, expr);
  }

  @Test
  public void testArray() {
    ExprNode expr = ExpressionParser.parse("array[100 + 2]").optimize();
    ArrayNode expected = new ArrayNode(new VarNode("array"), new IndexNode(new IntConstant(BigInteger.valueOf(102))));
    assertEquals(expected, expr);
  }
}