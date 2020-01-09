package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpressionList implements ExprNode {
  private static final ExpressionList EMPTY = new ExpressionList();

  public final List<ExprNode> expressions = new ArrayList<>();

  public ExpressionList(ExprNode first, ExprNode second) {
    if (first instanceof ExpressionList) {
      expressions.addAll(((ExpressionList) first).expressions);
    } else {
      expressions.add(first);
    }
    expressions.add(second);
  }

  public ExpressionList(ExprNode element) {
    expressions.add(element);
  }

  private ExpressionList() {
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
  public void assignTo(Variable target, Assembler assembler, Assembler.Function function, String context) {
    expressions.get(0).assignTo(target, assembler, function, context);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.Function function, Set<TempVariable> tempParams) {
    return expressions.get(0).toParameter(assembler, function, tempParams);
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
