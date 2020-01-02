package intcode.assembler;

import java.util.List;

public class FunctionCall extends Op {
  private final Assembler assembler;
  private final String funcName;
  private final List<Parameter> parameters;
  private final List<Variable> outputs;
  private final String context;

  public FunctionCall(Assembler assembler, String funcName, List<Parameter> parameters, List<Variable> outputs, String context) {
    this.assembler = assembler;
    this.funcName = funcName;
    this.parameters = parameters;
    this.outputs = outputs;
    this.context = context;
    if (parameters.size() > 0) {
      assembler.getParam(parameters.size() - 1);
    }
    if (outputs.size() > 0) {
      assembler.getParam(outputs.size() - 1);
    }
  }

  @Override
  public int size() {
    return preCallSize() + AddOp.SIZE * outputs.size();
  }

  private int preCallSize() {
    return Jump.SIZE + AddOp.SIZE + AddOp.SIZE * parameters.size();
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
    Assembler.Function function = assembler.functions.get(funcName);
    if (function == null) {
      throw new RuntimeException("Could not find function " + funcName);
    }

    Jump jump = new Jump("# jump to function", false, Constant.ZERO, new Constant(function.getAddress()), null);
    AddOp returnAddress = new AddOp(context, new Constant(getAddress() + preCallSize()), Constant.ZERO, new StackVariable(0).setOffset(0));

    int i = 0;
    for (Parameter parameter : parameters) {
      new AddOp("# prepare for function call: stack " + i, parameter, Constant.ZERO, assembler.getParam(i)).writeTo(res);
      i++;
    }
    returnAddress.writeTo(res);
    jump.writeTo(res);
    i = 0;
    for (Variable output : outputs) {
      Variable returnValue = assembler.getParam(i);
      new AddOp("# copy return value " + i, returnValue, Constant.ZERO, output).writeTo(res);
      i++;
    }
  }
}
