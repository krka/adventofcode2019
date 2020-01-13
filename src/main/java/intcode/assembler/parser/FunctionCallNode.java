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

public class FunctionCallNode implements ExprNode {
  private final String funcName;
  private final ExpressionList parameters;

  public FunctionCallNode(String funcName, ExpressionList parameters) {
    this.funcName = funcName;
    this.parameters = parameters != null ? parameters : ExpressionList.empty();
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    List<ExprNode> returnVars = new ArrayList<>();
    returnVars.add(new VariableNode(target));
    FunctionCallStatement functionCallStatement = new FunctionCallStatement(funcName, parameters.expressions, returnVars);
    functionCallStatement.apply(assembler, function, context);
  }

  @Override
  public String toString() {
    return funcName + "(" + parameters + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FunctionCallNode that = (FunctionCallNode) o;
    return funcName.equals(that.funcName) &&
            parameters.equals(that.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(funcName, parameters);
  }

  public String getFuncName() {
    return funcName;
  }

  public ExpressionList getParameters() {
    return parameters;
  }
}
