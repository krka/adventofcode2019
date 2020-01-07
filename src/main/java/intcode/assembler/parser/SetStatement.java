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
    if (target instanceof VarNode) {
      VarNode varNode = (VarNode) this.target;
      Variable variable = function.resolveVariable(varNode.getName());
      expr.assignTo(variable, assembler, function, context);
    } else if (target instanceof ArrayNode) {
      ArrayNode arrayNode = (ArrayNode) target;

      Set<TempVariable> tempParams = new HashSet<>();
      Parameter arrayParam = arrayNode.getArray().toParameter(assembler, function, tempParams);
      Parameter indexParam = arrayNode.getIndex().toParameter(assembler, function, tempParams);
      Parameter valueParam = expr.toParameter(assembler, function, tempParams);

      AddOp rewriteParam = new AddOp(context, arrayParam, indexParam, Constant.PLACEHOLDER_POSITION);
      SetOp addOp = new SetOp("# write to array from value", valueParam, Constant.PLACEHOLDER_POSITION);

      rewriteParam.setTarget(DeferredParameter.ofInt(ParameterMode.POSITION, () -> addOp.getAddress() + 3));
      function.operations.add(rewriteParam);
      function.operations.add(addOp);
      tempParams.forEach(TempVariable::release);
    } else {
      throw new RuntimeException("Can't assign a value to " + target);
    }
  }
}
