package intcode.assembler.parser;

import intcode.assembler.Assembler;

import java.util.Objects;

public class SetStatement {
  private final ExprNode target;
  private final ExprNode expr;

  public SetStatement(ExprNode target, ExprNode expr) {
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

  public void apply(Assembler assembler, Assembler.Function function, String context) {
    target.assignValue(assembler, function, context, expr);
  }

  public boolean valid() {
    return target.canAssign();
  }
}
