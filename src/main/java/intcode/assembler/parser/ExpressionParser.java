package intcode.assembler.parser;

import NegNode.NegNode;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;
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
            .prefix(CharacterParser.of('-').trim(), (List<Object> o) -> new NegNode((ExprNode) o.get(1)));

    // TODO: implement arrays
    //expressionBuilder.group().wrapper(IDENTIFIER.trim().seq(CharacterParser.of('[').trim()), CharacterParser.of(']').trim());

    expressionBuilder.group()
            .left(CharacterParser.of('*').trim(), (List<Object> o) -> new MulNode((ExprNode) o.get(0), (ExprNode) o.get(2)));

    expressionBuilder.group()
            .left(CharacterParser.of('+').trim(), (List<Object> o) -> new AddNode((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(CharacterParser.of('-').trim(), (List<Object> o) -> new AddNode((ExprNode) o.get(0), new NegNode((ExprNode) o.get(2))));

    EXPRESSION = expressionBuilder.build().trim().end();

    SET_STATEMENT = IDENTIFIER.flatten().map(VarNode::new).seq(CharacterParser.of('=').trim()).seq(EXPRESSION).end()
    .map((List<Object> o) -> new SetStatement((VarNode) o.get(0), (ExprNode) o.get(2)));
  }

  public static ExprNode parse(String s) {
    return EXPRESSION.parse(s).get();
  }

  public static SetStatement parseSetStatement(String line) {
    Result parse = SET_STATEMENT.parse(line);
    if (parse.isSuccess()) {
      return parse.get();
    }
    return null;
  }

}