package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpressionList implements ExprNode {
  private static final ExpressionList EMPTY = new ExpressionList();

  public final List<ExprNode> expressions = new ArrayList<>();

  public ExpressionList(List<ExprNode> nodes) {
    expressions.addAll(nodes);
  }

  public ExpressionList(ExprNode... nodes) {
    this(Arrays.asList(nodes));
  }

  private ExpressionList() {
  }

  public static ExpressionList parse(List<Object> objects) {
    ArrayList<ExprNode> expressions = new ArrayList<>();
    ExprNode head = (ExprNode) objects.get(0);
    expressions.add(head);

    List<Object> tail = (List<Object>) objects.get(1);
    for (Object tailNode : tail) {
      expressions.add((ExprNode) ((List<Object>) tailNode).get(1));
    }
    return new ExpressionList(expressions);
  }

  @Override
  public void assignValue(Assembler assembler, Assembler.IntCodeFunction function, String context, ExprNode expr) {
    int targetSize = expressions.size();

    List<ExprNode> source = expr.toExpressionList().expressions;
    int sourceSize = source.size();

    if (targetSize == sourceSize) {
      // We can skip temp vars here
      if (targetSize == 1) {
        expressions.get(0).assignValue(assembler, function, context, expr);
        return;
      }

      ArrayList<TempVariable> temps = new ArrayList<>();
      for (ExprNode exprNode : source) {
        TempVariable temp = assembler.tempSpace.getAny();
        temps.add(temp);
        exprNode.assignTo(temp, assembler, function, context);
      }
      for (int i = 0; i < targetSize; i++) {
        expressions.get(i).assignValue(assembler, function, context, new VariableNode(temps.get(i)));
      }
      temps.forEach(TempVariable::release);

      return;
    }

    // target size is greater than source size - last must be a function with enough return values!
    ExprNode lastSource = source.get(sourceSize - 1);

    if (lastSource instanceof FunctionCallNode) {
      FunctionCallNode func = (FunctionCallNode) lastSource;

      int numCopies = sourceSize - 1;

      ArrayList<TempVariable> temps = new ArrayList<>();
      for (ExprNode exprNode : source) {
        TempVariable temp = assembler.tempSpace.getAny();
        temps.add(temp);
        if (exprNode != func) {
          exprNode.assignTo(temp, assembler, function, context);
        }
      }

      // Make the final function call
      List<ExprNode> returnVars = new ArrayList<>();
      for (int i = numCopies; i < targetSize; i++) {
        returnVars.add(expressions.get(i));
      }
      FunctionCallStatement statement = new FunctionCallStatement(func.getFuncName(), func.getParameters().expressions, returnVars);
      statement.apply(assembler, function, context);

      for (int i = 0; i < numCopies; i++) {
        expressions.get(i).assignValue(assembler, function, context, new VariableNode(temps.get(i)));
      }
      temps.forEach(TempVariable::release);
    } else {
      throw new RuntimeException("Can't assign " + sourceSize + " values to " + targetSize + " targets");
    }
  }

  public static ExpressionList empty() {
    return EMPTY;
  }

  @Override
  public ExpressionList optimize() {
    for (int i = 0; i < expressions.size(); i++) {
      expressions.set(i, expressions.get(i).optimize());
    }
    return this;
  }

  @Override
  public BigInteger value() {
    return expressions.get(0).value();
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    expressions.get(0).assignTo(target, assembler, function, context);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.IntCodeFunction function, Set<TempVariable> tempParams) {
    return expressions.get(0).toParameter(assembler, function, tempParams);
  }

  @Override
  public ExpressionList toExpressionList() {
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExpressionList that = (ExpressionList) o;
    return expressions.equals(that.expressions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expressions);
  }

  @Override
  public String toString() {
    return expressions.stream().map(Objects::toString).collect(Collectors.joining(", "));
  }
}
