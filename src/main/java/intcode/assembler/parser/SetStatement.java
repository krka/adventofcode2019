package intcode.assembler.parser;

import intcode.assembler.Assembler;

import java.util.Objects;

public class SetStatement implements Statement {
  private final ExpressionList target;
  private final ExpressionList expr;

  public SetStatement(ExpressionList target, ExpressionList expr) {
    this.target = target.optimize();
    this.expr = expr.optimize();
  }

  @Override
  public String toString() {
    return target + " = " + expr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SetStatement that = (SetStatement) o;
    return target.equals(that.target) &&
            expr.equals(that.expr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(target, expr);
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    target.assignValue(assembler, caller, context, expr);
  }

  public ExpressionList getTarget() {
    return target;
  }

  public ExpressionList getExpr() {
    return expr;
  }
}
