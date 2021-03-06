package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Variable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ReturnStatement implements Statement {

  private final List<ExprNode> returnValues;

  public ReturnStatement(ExpressionList returnValues) {
    if (returnValues == null) {
      this.returnValues = Collections.emptyList();
    } else {
      this.returnValues = returnValues.expressions;
    }
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    // Copy return values to temp param space
    List<Variable> params = assembler.paramSpace.get(returnValues.size());
    for (int i = 0; i < returnValues.size(); i++) {
      returnValues.get(i).assignTo(params.get(i), assembler, caller, "# copy return value " + i + " to param space");
    }

    caller.addReturn(context, returnValues.size());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReturnStatement that = (ReturnStatement) o;
    return returnValues.equals(that.returnValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(returnValues);
  }

  @Override
  public String toString() {
    return "return " + returnValues;
  }
}
