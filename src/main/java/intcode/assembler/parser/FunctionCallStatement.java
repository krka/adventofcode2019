package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Variable;

import java.util.List;

public class FunctionCallStatement implements Statement {
  private final List<ExprNode> returnVars;
  private final String funcName;
  private final List<ExprNode> parameters;

  public FunctionCallStatement(String funcName, List<ExprNode> parameters, List<ExprNode> returnVars) {
    this.funcName = funcName;
    this.parameters = parameters;
    this.returnVars = returnVars;
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    // Copy parameters
    List<Variable> targetParams = assembler.paramSpace.get(parameters.size());
    int i = 0;
    for (ExprNode parameter : parameters) {
      Variable target = targetParams.get(i);
      parameter.assignTo(target, assembler, caller, "# prepare for function call: param " + i);
      i++;
    }

    caller.addFunctionCall(funcName, parameters.size(), returnVars.size(), context);

    List<Variable> returnValues = assembler.paramSpace.get(returnVars.size());
    i = 0;
    for (ExprNode returnVar : returnVars) {
      Variable source = returnValues.get(i);
      returnVar.assignValue(assembler, caller, "# copy back return value " + i, new VariableNode(source));
      i++;
    }
  }

}
