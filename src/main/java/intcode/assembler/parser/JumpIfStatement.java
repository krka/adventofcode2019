package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Label;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;

import java.util.HashSet;

public class JumpIfStatement implements Statement {
  private static String labelString;
  private final ExprNode condition;
  private final boolean isTrue;
  private final Label label;

  private JumpIfStatement(boolean isTrue, ExprNode condition, Label label, String labelString) {
    this.isTrue = isTrue;
    this.condition = condition;
    this.label = label;
    this.labelString = labelString;
  }

  public static JumpIfStatement create(ExprNode condition, Label label, String labelString) {
    if (condition instanceof NotNode) {
      return new JumpIfStatement(false, ((NotNode) condition).getChild(), label, labelString);
    }
    return new JumpIfStatement(true, condition, label, labelString);
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    HashSet<TempVariable> tempParams = new HashSet<>();
    Parameter parameter = condition.toParameter(assembler, caller, tempParams);
    caller.jump(isTrue, parameter, getLabel(caller), context);
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

  Label getLabel(Assembler.IntCodeFunction function) {
    if (label != null) {
      return label;
    }
    return function.resolveLabel(labelString);
  }

  public String getLabelString() {
    return labelString;
  }
}
