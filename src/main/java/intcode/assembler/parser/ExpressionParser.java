package intcode.assembler.parser;

import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;
import org.petitparser.parser.primitive.StringParser;
import org.petitparser.tools.ExpressionBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

  static {
    ExpressionBuilder expressionBuilder = new ExpressionBuilder();
    expressionBuilder.group()
            .primitive(IDENTIFIER.flatten().map(VarNode::new))
            .primitive(POS_INT.flatten().map((String s) -> new IntConstant(new BigInteger(s))));

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

    EXPRESSION = expressionBuilder.build().trim();

    SET_STATEMENT = EXPRESSION.trim().seq(CharacterParser.of('=').trim()).seq(EXPRESSION.trim())
      .map((List<Object> o) -> new SetStatement((ExprNode) o.get(0), (ExprNode) o.get(2)));

    EXPRESSION_LIST = EXPRESSION.trim().seq(CharacterParser.of(',').trim().seq(EXPRESSION.trim()).star())
            .map((List<Object> o) -> flattenExpr(o));

    Parser assigningToSomething = EXPRESSION_LIST.trim().seq(CharacterParser.of('=').trim());
    Parser theCall = IDENTIFIER.flatten().trim()
            .seq(CharacterParser.of('(').trim())
            .seq(EXPRESSION_LIST.optional())
            .seq(CharacterParser.of(')').trim())
            .map((List<Object> o) -> new FunctionCallStatement((String) o.get(0), flattenExpr(o.get(2))));

    FUNCTION_CALL = assigningToSomething.optional()
            .seq(theCall)
            .map((List<Object> o) -> ((FunctionCallStatement) o.get(1)).withReturnValues(flattenExpr(o.get(0))));

    JUMP_IF = StringParser.of("if").flatten().trim()
            .seq(EXPRESSION)
            .seq(StringParser.of("jump").flatten().trim())
            .seq(IDENTIFIER.flatten().trim())
            .map((List<Object> o) -> new JumpIfStatement((ExprNode) o.get(1), (String) o.get(3)));
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

  public static ExprNode parse(String s) {
    return EXPRESSION.end().parse(s).get();
  }

  public static SetStatement parseSetStatement(String line) {
    Result parse = SET_STATEMENT.end().parse(line);
    if (parse.isSuccess()) {
      return parse.get();
    }
    return null;
  }

  public static FunctionCallStatement parseFunctionCall(String line) {
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

}
