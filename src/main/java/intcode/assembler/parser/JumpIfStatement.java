package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;

import java.util.HashSet;

public class JumpIfStatement implements Statement {
  private final ExprNode condition;
  private final boolean isTrue;
  private final String label;

  public JumpIfStatement(ExprNode condition, String label) {
    if (condition instanceof NotNode) {
      this.condition = ((NotNode) condition).getChild().optimize();
      this.isTrue = false;
    } else {
      this.condition = condition.optimize();
      this.isTrue = true;
    }
    this.label = label;
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    HashSet<TempVariable> tempParams = new HashSet<>();
    Parameter parameter = condition.toParameter(assembler, caller, tempParams);
    caller.jump(isTrue, parameter, label, context);
    tempParams.forEach(TempVariable::release);
  }

  @Override
  public String toString() {
    return "JumpIfStatement{" +
            "condition=" + condition +
            ", isTrue=" + isTrue +
            ", label='" + label + '\'' +
            '}';
  }

  public ExprNode getCondition() {
    return condition;
  }

  public boolean isTrue() {
    return isTrue;
  }

  public String getLabel() {
    return label;
  }
}
