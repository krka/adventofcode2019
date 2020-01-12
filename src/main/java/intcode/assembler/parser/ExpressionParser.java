package intcode.assembler.parser;

import org.petitparser.context.Result;
import org.petitparser.parser.Parser;
import org.petitparser.parser.combinators.SettableParser;
import org.petitparser.parser.primitive.CharacterParser;
import org.petitparser.parser.primitive.StringParser;
import org.petitparser.tools.ExpressionBuilder;

import java.math.BigInteger;
import java.util.List;

public class ExpressionParser {

  private static final Parser IDENTIFIER_TAIL_PART = CharacterParser.letter().or(CharacterParser.digit(), CharacterParser.of('_'));
  private static final Parser IDENTIFIER_TAIL = IDENTIFIER_TAIL_PART.star();
  private static final Parser IDENTIFIER = CharacterParser.letter().seq(IDENTIFIER_TAIL).flatten().trim();

  private static final Parser POS_INT = CharacterParser.digit().plus().flatten().trim();

  private static final Parser STATEMENT;
  private static final Parser EXPRESSION;

  static {
    SettableParser expression = SettableParser.undefined();
    SettableParser expressionList = SettableParser.undefined();

    ExpressionBuilder expressionBuilder = new ExpressionBuilder();
    Parser varParser = IDENTIFIER.map(VarNode::new);
    Parser constantParser = POS_INT.map((String s) -> new IntConstant(new BigInteger(s)));
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
            .prefix(CharacterParser.of('-').trim(), (List<Object> o) -> NegNode.create((ExprNode) o.get(1)))
            .prefix(CharacterParser.of('!').trim(), (List<Object> o) -> NotNode.create((ExprNode) o.get(1)));

    expressionBuilder.group()
            .left(CharacterParser.of('*').trim(), (List<Object> o) -> MulNode.create((ExprNode) o.get(0), (ExprNode) o.get(2)));

    expressionBuilder.group()
            .left(CharacterParser.of('+').trim(), (List<Object> o) -> AddNode.create((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(CharacterParser.of('-').trim(), (List<Object> o) -> AddNode.create((ExprNode) o.get(0), NegNode.create((ExprNode) o.get(2))));

    // relational
    expressionBuilder.group()
            .left(StringParser.of("<=").trim(), (List<Object> o) -> NotNode.create(GreaterThanNode.create((ExprNode) o.get(0), (ExprNode) o.get(2))))
            .left(StringParser.of(">=").trim(), (List<Object> o) -> NotNode.create(LessThanNode.create((ExprNode) o.get(0), (ExprNode) o.get(2))))
            .left(StringParser.of("<").trim(), (List<Object> o) -> LessThanNode.create((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(StringParser.of(">").trim(), (List<Object> o) -> GreaterThanNode.create((ExprNode) o.get(0), (ExprNode) o.get(2)));

    // equality
    expressionBuilder.group()
            .left(StringParser.of("==").trim(), (List<Object> o) -> EqNode.create((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(StringParser.of("!=").trim(), (List<Object> o) -> NotNode.create(EqNode.create((ExprNode) o.get(0), (ExprNode) o.get(2))));

    // logical
    expressionBuilder.group()
            .left(StringParser.of("&&").trim(), (List<Object> o) -> AndNode.create((ExprNode) o.get(0), (ExprNode) o.get(2)))
            .left(StringParser.of("||").trim(), (List<Object> o) -> OrNode.create((ExprNode) o.get(0), (ExprNode) o.get(2)));

    expression.set(expressionBuilder.build().trim());

    Parser commaAndExpression = CharacterParser.of(',').trim().seq(expression).star();
    expressionList.set(expression.seq(commaAndExpression).map(ExpressionList::parse));

    Parser setStatement = expressionList.trim().seq(CharacterParser.of('=').trim()).seq(expressionList.trim())
            .end()
      .map((List<Object> o) -> new SetStatement((ExpressionList) o.get(0), (ExpressionList) o.get(2)));

    Parser functionCall = functionCallParser
            .end()
            .map((FunctionCallNode func) -> new SetStatement(ExpressionList.empty(), func.toExpressionList()));

    Parser jumpIfStatement = StringParser.of("if").flatten().trim()
            .seq(expression)
            .seq(StringParser.of("jump").flatten().trim())
            .seq(IDENTIFIER)
            .end()
            .map((List<Object> o) -> JumpIfStatement.create((ExprNode) o.get(1), null, (String) o.get(3)));

    Parser returnStatement = StringParser.of("return ").trim()
            .seq(expressionList)
            .end()
            .pick(1)
            .map(o -> new ReturnStatement((ExpressionList) o));

    Parser returnNoneStatement = StringParser.of("return").trim().end()
            .end()
            .map(o -> new ReturnStatement(ExpressionList.empty()));

    Parser declareInt = StringParser.of("int").trim().seq(setStatement)
            .end()
            .map((List<Object> o) -> new DeclareIntStatement((SetStatement) o.get(1)));

    Parser filename = CharacterParser.letter().or(CharacterParser.of('.')).plus().flatten().trim();
    Parser includeResource = StringParser.of("#include").trim().seq(filename)
            .end()
            .map((List<Object> o) -> new IncludeResourceStatement((String) o.get(1)));


    Parser comment = CharacterParser.of('#').trim().seq(CharacterParser.any().star())
            .end()
            .map(o -> new CommentStatement());

    Parser label = IDENTIFIER.seq(CharacterParser.of(':'))
            .end()
            .map((List<Object> o) -> new LabelStatement((String) o.get(0)));

    Parser jumpAlways = StringParser.of("jump").flatten().trim().seq(IDENTIFIER)
            .end()
            .map((List<Object> o) -> JumpIfStatement.create(IntConstant.ONE, null, (String) o.get(1)));

    Parser declareString = StringParser.of("string").flatten().trim()
            .seq(IDENTIFIER)
            .seq(CharacterParser.of('=')).trim()
            .seq(CharacterParser.of('"'))
            .seq(CharacterParser.noneOf("\"").star().flatten())
            .seq(CharacterParser.of('"'))
            .end()
            .map((List<Object> o) -> new DeclareStringStatement((String) list(o.get(0)).get(1), (String) o.get(2)));

    Parser declareArray = StringParser.of("array").flatten().trim()
            .seq(CharacterParser.of('[').trim())
            .seq(expression)
            .seq(CharacterParser.of(']').trim())
            .seq(IDENTIFIER)
            .end()
            .map((List<Object> o) -> new DeclareArrayStatement((ExprNode) o.get(2), (String) o.get(4)));

    Parser functionDefinition = StringParser.of("func").trim()
            .seq(IDENTIFIER)
            .seq(CharacterParser.of('(').trim())
            .seq(expressionList.optional())
            .seq(CharacterParser.of(')').trim())
            .end()
            .map((List<Object> o) -> new FunctionDefinitionStatement((String) o.get(1), (ExpressionList) o.get(3)));

    Parser endBlock = StringParser.of("end").trim().end()
            .map(o -> new EndBlockStatement());

    Parser breakBlock = StringParser.of("break").trim().end()
            .map(o -> new BreakBlockStatement());

    Parser elseBlock = StringParser.of("else").trim().end()
            .map(o -> new ElseStatement());

    Parser startIfBlock = StringParser.of("if").trim()
            .seq(expression)
            .seq(StringParser.of("then").trim())
            .end()
            .pick(1)
            .map(o -> new StartIfBlockStatement((ExprNode) o));

    Parser elseIfBlock = StringParser.of("elseif").trim()
            .seq(expression)
            .seq(StringParser.of("then").trim())
            .end()
            .pick(1)
            .map((o -> new ElseIfBlockStatement((ExprNode) o)));

    Parser startWhileBlock = StringParser.of("while").trim()
            .seq(expression)
            .end()
            .pick(1)
            .map((o -> new StartWhileBlockStatement((ExprNode) o)));

    Parser startForLoop = StringParser.of("for").trim()
            .seq(varParser)
            .seq(CharacterParser.of('=').trim())
            .seq(expression)
            .seq(StringParser.of("while"))
            .seq(expression)
            .seq(StringParser.of("step"))
            .seq(expression)
            .seq(StringParser.of("do"))
            .map(((List<Object> o) -> new StartForLoopBlockStatement((VarNode) o.get(1), (ExprNode) o.get(3), (ExprNode) o.get(5), (ExprNode) o.get(7))));

    STATEMENT = functionCall.or(
            includeResource, comment,
            returnStatement, returnNoneStatement,
            setStatement, jumpIfStatement,
            declareInt, declareString, declareArray,
            functionDefinition, endBlock,
            label, jumpAlways,
            startIfBlock, elseBlock, elseIfBlock,
            startWhileBlock, breakBlock, startForLoop
            );
    EXPRESSION = expressionList;
  }

  private static List<Object> list(Object o) {
    return (List<Object>) o;
  }

  public static ExprNode parseExpr(String s) {
    Result result = EXPRESSION.end().parse(s);
    if (result.isSuccess()) {
      return result.get();
    }
    return null;
  }

  public static Statement parseStatement(String line) {
    Result parse = STATEMENT.end().parse(line);
    if (parse.isSuccess()) {
      return parse.get();
    }
    throw new RuntimeException(parse.getMessage());
  }
}
