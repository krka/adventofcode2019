package intcode.assembler.parser;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ExpressionParserTest {

  @Test
  public void testParser() {
    ExprNode expr = ExpressionParser.parseExpr("10 * 123 + -456 - x");
    MulNode muls = new MulNode(new IntConstant(BigInteger.valueOf(10)), new IntConstant(BigInteger.valueOf(123)));
    ExprNode expected = new AddNode(new AddNode(muls, new NegNode(new IntConstant(BigInteger.valueOf(456)))), new NegNode(new VarNode("x")));
    assertEquals(expected, expr);
  }

  @Test
  public void testOptimize() {
    ExprNode expr = ExpressionParser.parseExpr("10 * 123 + -456 - x").optimize();
    ExprNode expected = ExpressionParser.parseExpr("774 - x").optimize();
    assertEquals(expected, expr);
  }

  @Test
  public void testSetStatement() {
    SetStatement setStatement = ExpressionParser.parseSetStatement("x = y + 3 + 5");
    assertEquals("x = y + 3 + 5", setStatement.toString());
  }

  @Test
  public void testParen() {
    ExprNode expr = ExpressionParser.parseExpr("10 * (123 + 5)").optimize();
    ExprNode expected = ExpressionParser.parseExpr("1280").optimize();
    assertEquals(expected, expr);
  }

  @Test
  public void testArray() {
    ExprNode expr = ExpressionParser.parseExpr("array[100 + 2]").optimize();
    ArrayNode expected = new ArrayNode(new VarNode("array"), new IndexNode(new IntConstant(BigInteger.valueOf(102))));
    assertEquals(expected, expr);
  }

  @Test
  public void testFunctionCall() {
    SetStatement call = ExpressionParser.parseFunctionCall("foo()");
    assertEquals(ExpressionList.empty(), call.getTarget());
    assertEquals(new ExpressionList(new FunctionCallNode("foo", ExpressionList.empty())), call.getExpr());
  }

  @Test
  public void testParseExpressionWithFunctionCall() {
    ExprNode exprNode = ExpressionParser.parseExpr("foo()");
    assertEquals(new FunctionCallNode("foo", ExpressionList.empty()), exprNode);
  }

  @Test
  public void testParseExpressionWithFunctionCallWithParameter1() {
    ExprNode exprNode = ExpressionParser.parseExpr("foo(1)");
    assertEquals(new FunctionCallNode("foo", new ExpressionList(IntConstant.ONE)), exprNode);
    System.out.println(exprNode);
  }

  @Test
  public void testParseExpressionWithFunctionCallWithParameter2() {
    ExprNode exprNode = ExpressionParser.parseExpr("foo(1, 2)");
    assertEquals(new FunctionCallNode("foo", new ExpressionList(IntConstant.ONE, new IntConstant(BigInteger.TWO))), exprNode);
  }

  @Test
  public void testParseExpressionWithFunctionCallWithParameter3() {
    ExprNode exprNode = ExpressionParser.parseExpr("foo(1, 2, 0)");
    assertEquals(new FunctionCallNode("foo", new ExpressionList(IntConstant.ONE, new IntConstant(BigInteger.TWO), IntConstant.ZERO)), exprNode);
  }

  @Test
  public void testFunctionCallWithParameters() {
    SetStatement call = ExpressionParser.parseFunctionCall("foo(0, 1, 2)");
    assertEquals(ExpressionList.empty(), call.getTarget());
    assertEquals(new ExpressionList(new FunctionCallNode("foo", new ExpressionList(IntConstant.ZERO, IntConstant.ONE, IntConstant.TWO))), call.getExpr());
  }

  @Test
  public void testFunctionCallWithReturnValues() {
    SetStatement call = ExpressionParser.parseSetStatement("a, b = foo(0, 1, 2)");
    assertEquals(new ExpressionList(new VarNode("a"), new VarNode("b")), call.getTarget());
    assertEquals(new ExpressionList(new FunctionCallNode("foo", new ExpressionList(IntConstant.ZERO, IntConstant.ONE, IntConstant.TWO))), call.getExpr());
  }

  @Test
  public void testJumpIf() {
    JumpIfStatement statement = ExpressionParser.parseJumpIf("if a == b jump foo");
    assertEquals("foo", statement.getLabel());
    assertEquals(ExpressionParser.parseExpr("a == b").optimize(), statement.getCondition());
  }

  @Test
  public void testFuncExpr() {
    ExprNode expression = ExpressionParser.parseExpr("foo(0, 1)");
    ExprNode expected = new FunctionCallNode("foo", new ExpressionList(IntConstant.ZERO, IntConstant.ONE));
    assertEquals(expected, expression);
  }

  @Test
  public void testFuncExpr2() {
    ExprNode expression = ExpressionParser.parseExpr("foo(0, 1) + bar()");
    ExprNode expected = new AddNode(new FunctionCallNode("foo", new ExpressionList(IntConstant.ZERO, IntConstant.ONE)), new FunctionCallNode("bar", ExpressionList.empty()));
    assertEquals(expected, expression);
  }

  @Test
  public void testPrecedence() {
    ExprNode expression = ExpressionParser.parseExpr("a == x || y");
    ExprNode expected = new OrNode(new EqNode(new VarNode("a"), new VarNode("x")), new VarNode("y"));
    assertEquals(expected, expression);
  }
}
