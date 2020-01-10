package intcode.assembler.parser;

import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.combinators.ChoiceParser;
import org.petitparser.parser.combinators.SettableParser;
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

  private static final Parser STATEMENT;
  private static final Parser EXPRESSION;

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
    expressionList.set(expression.seq(commaAndExpression).map(ExpressionList::parse));

    Parser setStatement = expressionList.trim().seq(CharacterParser.of('=').trim()).seq(expressionList.trim())
      .map((List<Object> o) -> new SetStatement((ExpressionList) o.get(0), (ExpressionList) o.get(2)));

    Parser functionCall = functionCallParser.map((FunctionCallNode func) -> new SetStatement(ExpressionList.empty(), func.toExpressionList()));

    Parser jumpIfStatement = StringParser.of("if").flatten().trim()
            .seq(expression)
            .seq(StringParser.of("jump").flatten().trim())
            .seq(IDENTIFIER.flatten().trim())
            .map((List<Object> o) -> new JumpIfStatement((ExprNode) o.get(1), (String) o.get(3)));

    Parser returnStatement = StringParser.of("return").flatten().trim()
            .seq(expressionList.optional())
            .map((List<Object> o) -> new ReturnStatement((ExpressionList) o.get(1)));

    Parser declareInt = StringParser.of("int").trim().seq(setStatement)
            .map((List<Object> o) -> new DeclareIntStatement((SetStatement) o.get(1)));

    Parser filename = CharacterParser.letter().or(CharacterParser.of('.')).plus().flatten().trim();
    Parser includeResource = StringParser.of("#include").trim().seq(filename)
            .map((List<Object> o) -> new IncludeResourceStatement((String) o.get(1)));


    Parser comment = CharacterParser.of('#').trim().seq(CharacterParser.any()).map(o -> new CommentStatement());

    STATEMENT = functionCall.or(setStatement, jumpIfStatement, returnStatement, declareInt, includeResource, comment);
    EXPRESSION = expressionList;
  }

  public static ExprNode parseExpr(String s) {
    Result result = EXPRESSION.end().parse(s);
    if (result.isSuccess()) {
      return result.get();
    }
    return null;
  }

  public static Statement parseStatement(String line) {
    Result parse = STATEMENT.parse(line);
    if (parse.isSuccess()) {
      return parse.get();
    }
    throw new RuntimeException(parse.getMessage());
  }
}
