package intcode.assembler.parser;

import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.combinators.SequenceParser;
import org.petitparser.parser.combinators.SettableParser;
import org.petitparser.parser.primitive.CharacterParser;
import org.petitparser.parser.primitive.StringParser;
import org.petitparser.tools.ExpressionBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ExpressionParser {

  private static final Parser IDENTIFIER_TAIL_PART = CharacterParser.letter().or(CharacterParser.digit(), CharacterParser.of('_'));
  private static final Parser IDENTIFIER_TAIL = IDENTIFIER_TAIL_PART.star();
  private static final Parser IDENTIFIER = CharacterParser.letter().seq(IDENTIFIER_TAIL);

  private static final Parser POS_INT = CharacterParser.digit().plus();

  private static final Parser EXPRESSION;
  private static final Parser SET_STATEMENT;
  private static final Parser EXPRESSION_LIST;
  private static final Parser FUNCTION_CALL;
  private static final Parser JUMP_IF;
  private static final Parser RETURN;

  static {
    SettableParser expression = SettableParser.undefined();
    SettableParser expressionList = SettableParser.undefined();

    ExpressionBuilder expressionBuilder = new ExpressionBuilder();
    Parser varParser = IDENTIFIER.flatten().map(VarNode::new);
    Parser constantParser = POS_INT.flatten().map((String s) -> new IntConstant(new BigInteger(s)));
    Parser functionCallParser = varParser.seq(CharacterParser.of('(').trim(), expressionList.optional(), CharacterParser.of(')').trim())
        .map((List<Object> o) -> new FunctionCallNode(((VarNode) o.get(0)).getName(), (ExpressionList) o.get(2)));

    expressionBuilder.group()
            .primitive(functionCallParser)
            .primitive(varParser)
            .primitive(constantParser);

    expressionBuilder.group()
            .wrapper(CharacterParser.of('(').trim(), CharacterParser.of(')').trim(), (List<Object> o) -> o.get(1));

    expressionBuilder.group()
            .wrapper(CharacterParser.of('[').trim(), CharacterParser.of(']').trim(), (List<Object> o) -> new IndexNode((ExprNode) o.get(1)));

    expressionBuilder.group()
            .left(CharacterParser.of('[').and(), (List<Object> o) -> new ArrayNode((ExprNode) o.get(0), (IndexNode) o.get(2)));

    expressionBuilder.group()
            .prefix(CharacterParser.of('-').trim(), (List<Object> o) -> new NegNode((ExprNode) o.get(1)))
            .prefix(CharacterParser.of('!').trim(), (List<Object> o) -> new NotNode((ExprNode) o.get(1)));

    expressionBuilder.group()
            .left(CharacterParser.of('*').trim(), (List<Object> o) -> new MulNode((ExprNode) o.get(0), (ExprNode) o.get(2)));

    expressionBuilder.group()
            .left(CharacterParser.of('+').trim(), (List<Object> o) -> new AddNode((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(CharacterParser.of('-').trim(), (List<Object> o) -> new AddNode((ExprNode) o.get(0), new NegNode((ExprNode) o.get(2))));

    // relational
    expressionBuilder.group()
            .left(StringParser.of("<=").trim(), (List<Object> o) -> new NotNode(new GreaterThanNode((ExprNode) o.get(0), (ExprNode) o.get(2))))
            .left(StringParser.of(">=").trim(), (List<Object> o) -> new NotNode(new LessThanNode((ExprNode) o.get(0), (ExprNode) o.get(2))))
            .left(StringParser.of("<").trim(), (List<Object> o) -> new LessThanNode((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(StringParser.of(">").trim(), (List<Object> o) -> new GreaterThanNode((ExprNode) o.get(0), (ExprNode) o.get(2)));

    // equality
    expressionBuilder.group()
            .left(StringParser.of("==").trim(), (List<Object> o) -> new EqNode((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(StringParser.of("!=").trim(), (List<Object> o) -> new NotNode(new EqNode((ExprNode) o.get(0), (ExprNode) o.get(2))));

    // logical
    expressionBuilder.group()
            .left(StringParser.of("&&").trim(), (List<Object> o) -> new AndNode((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(StringParser.of("||").trim(), (List<Object> o) -> new OrNode((ExprNode) o.get(0), (ExprNode) o.get(2)));

    expression.set(expressionBuilder.build().trim());

    Parser commaAndExpression = CharacterParser.of(',').trim().seq(expression).star();
    expressionList.set(expression.seq(commaAndExpression).map((List<Object> o) -> new ExpressionList(o)));

    EXPRESSION = expression;
    EXPRESSION_LIST = expressionList;

    SET_STATEMENT = EXPRESSION_LIST.trim().seq(CharacterParser.of('=').trim()).seq(EXPRESSION_LIST.trim())
      .map((List<Object> o) -> new SetStatement((ExpressionList) o.get(0), (ExpressionList) o.get(2)));

    FUNCTION_CALL = functionCallParser.map((FunctionCallNode func) -> new SetStatement(ExpressionList.empty(), func.toExpressionList()));

    JUMP_IF = StringParser.of("if").flatten().trim()
            .seq(EXPRESSION)
            .seq(StringParser.of("jump").flatten().trim())
            .seq(IDENTIFIER.flatten().trim())
            .map((List<Object> o) -> new JumpIfStatement((ExprNode) o.get(1), (String) o.get(3)));

    RETURN = StringParser.of("return").flatten().trim()
            .seq(EXPRESSION_LIST.optional())
            .map((List<Object> o) -> new ReturnStatement((ExpressionList) o.get(1)));
  }

  public static List<ExprNode> flattenExpr(Object o) {
    List<ExprNode> res = new ArrayList<>();
    flattenExpr(res, o);
    return res;
  }

  private static void flattenExpr(List<ExprNode> res, Object o) {
    if (o == null) {
      return;
    }
    if (o instanceof List) {
      for (Object o2 : (List<Object>) o) {
        flattenExpr(res, o2);
      }
    } else if (o instanceof ExprNode) {
      res.add((ExprNode) o);
    }
  }

  public static ExprNode parseExpr(String s) {
    Result result = EXPRESSION.end().parse(s);
    if (result.isSuccess()) {
      return result.get();
    }
    return null;
  }

  public static Statement parseStatement(String line) {
    List<Callable<Statement>> statements = new ArrayList<>();
    statements.add(() -> parseSetStatement(line));
    statements.add(() -> parseFunctionCall(line));
    statements.add(() -> parseJumpIf(line));
    statements.add(() -> parseReturn(line));

    for (Callable<Statement> statement : statements) {
      try {
        Statement call = statement.call();
        if (call != null) {
          return call;
        }
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  public static SetStatement parseSetStatement(String line) {
    Result parse = SET_STATEMENT.end().parse(line);
    if (parse.isSuccess()) {
      SetStatement res = parse.get();
      if (res.valid()) {
        return res;
      }
    }
    return null;
  }

  public static SetStatement parseFunctionCall(String line) {
    Result parse = FUNCTION_CALL.end().parse(line);
    if (parse.isSuccess()) {
      return parse.get();
    }
    return null;
  }

  public static JumpIfStatement parseJumpIf(String line) {
    Result parse = JUMP_IF.end().parse(line);
    if (parse.isSuccess()) {
      return parse.get();
    }
    return null;
  }

  public static ReturnStatement parseReturn(String line) {
    Result parse = RETURN.end().parse(line);
    if (parse.isSuccess()) {
      return parse.get();
    }
    return null;
  }

}
