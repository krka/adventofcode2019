package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Variable;

import java.util.Objects;

public class SetStatement {
  private final VarNode target;
  private final ExprNode expr;

  public SetStatement(VarNode target, ExprNode expr) {
    this.target = target;
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
    Variable variable = function.resolveVariable(target.getName());
    expr.assignTo(variable, assembler, function, context);
  }
}
