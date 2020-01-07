package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class FunctionCallStatement {
  private final List<ExprNode> returnVars;
  private final String funcName;
  private final List<ExprNode> parameters;

  public FunctionCallStatement(String funcName, List<ExprNode> parameters) {
    this.funcName = funcName;
    this.parameters = parameters;
    this.returnVars = Collections.emptyList();
  }

  public FunctionCallStatement(String funcName, List<ExprNode> parameters, List<ExprNode> returnVars) {
    this.funcName = funcName;
    this.parameters = parameters;
    this.returnVars = returnVars;
  }

  public void apply(Assembler assembler, Assembler.Function function, String context) {
    HashSet<TempVariable> tempParams = new HashSet<>();

    // Copy parameters
    List<Variable> targetParams = assembler.paramSpace.get(parameters.size());
    int i = 0;
    for (ExprNode parameter : parameters) {
      Variable target = targetParams.get(i);
      parameter.assignTo(target, assembler, function, "# prepare for function call: param " + i);
      i++;
    }

    function.addFunctionCall(funcName, parameters.size(), returnVars.size(), context);

    List<Variable> returnValues = assembler.paramSpace.get(returnVars.size());
    i = 0;
    for (ExprNode returnVar : returnVars) {
      Variable source = returnValues.get(i);
      returnVar.assignValue(assembler, function, "# copy back return value " + i, new VariableNode(source));
      i++;
    }
  }

  public List<ExprNode> getReturnVars() {
    return returnVars;
  }

  public String getFuncName() {
    return funcName;
  }

  public List<ExprNode> getParameters() {
    return parameters;
  }

  public FunctionCallStatement withReturnValues(List<ExprNode> returnValues) {
    return new FunctionCallStatement(funcName, parameters, returnValues);
  }

}
