package intcode.assembler.parser;

import NegNode.NegNode;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;
import org.petitparser.parser.primitive.StringParser;
import org.petitparser.tools.ExpressionBuilder;

import java.math.BigInteger;
import java.util.List;

public class ExpressionParser {

  private static final Parser IDENTIFIER_TAIL_PART = CharacterParser.letter().or(CharacterParser.digit(), CharacterParser.of('_'));
  private static final Parser IDENTIFIER_TAIL = IDENTIFIER_TAIL_PART.star();
  private static final Parser IDENTIFIER = CharacterParser.letter().seq(IDENTIFIER_TAIL);

  private static final Parser POS_INT = CharacterParser.digit().plus();

  private static final Parser EXPRESSION;
  private static final Parser SET_STATEMENT;

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
            .prefix(CharacterParser.of('-').trim(), (List<Object> o) -> new NegNode((ExprNode) o.get(1)));

    expressionBuilder.group()
            .left(CharacterParser.of('*').trim(), (List<Object> o) -> new MulNode((ExprNode) o.get(0), (ExprNode) o.get(2)));

    expressionBuilder.group()
            .left(CharacterParser.of('+').trim(), (List<Object> o) -> new AddNode((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(CharacterParser.of('-').trim(), (List<Object> o) -> new AddNode((ExprNode) o.get(0), new NegNode((ExprNode) o.get(2))));

    expressionBuilder.group()
            .left(StringParser.of("==").trim(), (List<Object> o) -> new EqNode((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(StringParser.of("<").trim(), (List<Object> o) -> new LessThanNode((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(StringParser.of(">").trim(), (List<Object> o) -> new GreaterThanNode((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(CharacterParser.of('-').trim(), (List<Object> o) -> new AddNode((ExprNode) o.get(0), new NegNode((ExprNode) o.get(2))));

    EXPRESSION = expressionBuilder.build().trim();

    SET_STATEMENT = EXPRESSION.trim().seq(CharacterParser.of('=').trim()).seq(EXPRESSION.trim())
      .map((List<Object> o) -> new SetStatement((ExprNode) o.get(0), (ExprNode) o.get(2)));
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

}
