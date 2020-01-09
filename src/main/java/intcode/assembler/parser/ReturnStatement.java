package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Variable;

import java.util.Collections;
import java.util.List;

public class ReturnStatement implements Statement {

  private final List<ExprNode> returnValues;

  public ReturnStatement(ExprNode returnValues) {
    if (returnValues == null) {
      this.returnValues = Collections.emptyList();
    } else {
      this.returnValues = returnValues.toExpressionList().expressions;
    }
  }

  @Override
  public void apply(Assembler assembler, Assembler.Function function, String context) {
    // Copy return values to temp param space
    List<Variable> params = assembler.paramSpace.get(returnValues.size());
    for (int i = 0; i < returnValues.size(); i++) {
      returnValues.get(i).assignTo(params.get(i), assembler, function, "# copy return value " + i + " to param space");
    }

    function.addReturn(context, returnValues.size());
  }
}
