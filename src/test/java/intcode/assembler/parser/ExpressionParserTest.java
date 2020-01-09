package intcode.assembler.parser;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

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

  @Test
  public void testFunctionCall() {
    FunctionCallStatement call = ExpressionParser.parseFunctionCall("foo()");
    assertEquals(Collections.emptyList(), call.getReturnVars());
    assertEquals(Collections.emptyList(), call.getParameters());
    assertEquals("foo", call.getFuncName());
  }

  @Test
  public void testFunctionCallWithParameters() {
    FunctionCallStatement call = ExpressionParser.parseFunctionCall("foo(0, 1, 2)");
    assertEquals(Collections.emptyList(), call.getReturnVars());
    assertEquals(Arrays.asList(IntConstant.ZERO, IntConstant.ONE, new IntConstant(BigInteger.valueOf(2))), call.getParameters());
    assertEquals("foo", call.getFuncName());
  }

  @Test
  public void testFunctionCallWithReturnValues() {
    FunctionCallStatement call = ExpressionParser.parseFunctionCall("a, b = foo(0, 1, 2)");
    assertEquals(Arrays.asList(new VarNode("a"), new VarNode("b")), call.getReturnVars());
    assertEquals(Arrays.asList(IntConstant.ZERO, IntConstant.ONE, new IntConstant(BigInteger.valueOf(2))), call.getParameters());
    assertEquals("foo", call.getFuncName());
  }

  @Test
  public void testJumpIf() {
    JumpIfStatement statement = ExpressionParser.parseJumpIf("if a == b jump foo");
    assertEquals("foo", statement.getLabel());
    assertEquals(ExpressionParser.parse("a == b").optimize(), statement.getCondition());
  }

  @Test
  public void testFuncExpr() {
    ExprNode expression = ExpressionParser.parse("foo(0, 1)");
    ExprNode expected = new FunctionCallNode("foo", new ExpressionList(IntConstant.ZERO, IntConstant.ONE));
    assertEquals(expected, expression);
  }

  @Test
  public void testFuncExpr2() {
    ExprNode expression = ExpressionParser.parse("foo(0, 1) + bar()");
    ExprNode expected = new AddNode(new FunctionCallNode("foo", new ExpressionList(IntConstant.ZERO, IntConstant.ONE)), new FunctionCallNode("bar", ExpressionList.empty()));
    assertEquals(expected, expression);
  }

  @Test
  public void testPrecedence() {
    ExprNode expression = ExpressionParser.parse("a == x || y");
    ExprNode expected = new OrNode(new EqNode(new VarNode("a"), new VarNode("x")), new VarNode("y"));
    assertEquals(expected, expression);
  }
}
