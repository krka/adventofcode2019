package intcode.assembler.parser;

import intcode.assembler.AddOp;
import intcode.assembler.Assembler;
import intcode.assembler.Constant;
import intcode.assembler.DeferredParameter;
import intcode.assembler.Parameter;
import intcode.assembler.ParameterMode;
import intcode.assembler.SetOp;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
}
