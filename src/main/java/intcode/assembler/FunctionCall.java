package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

public class FunctionCall extends Op {
  private final Assembler assembler;
  private final String funcName;
  private final List<Parameter> parameters;
  private final List<Variable> outputs;

  public FunctionCall(Assembler assembler, String funcName, List<Parameter> parameters, List<Variable> outputs) {
    this.assembler = assembler;
    this.funcName = funcName;
    this.parameters = parameters;
    this.outputs = outputs;
  }

  @Override
  public int size() {
    return preCallSize() + AddOp.SIZE * outputs.size();
  }

  private int preCallSize() {
    return Jump.SIZE + AddOp.SIZE + AddOp.SIZE * parameters.size();
  }

  @Override
  public void writeTo(List<BigInteger> res) {
    Assembler.Function function = assembler.functions.get(funcName);
    if (function == null) {
      throw new RuntimeException("Could not find function " + funcName);
    }

    Jump jump = new Jump(false, Constant.ZERO, new Constant(function.getAddress()), null);
    AddOp returnAddress = new AddOp(new Constant(getAddress() + preCallSize()), Constant.ZERO, new StackVariable(0));

    int i = 0;
    for (Parameter parameter : parameters) {
      i++;
      new AddOp(parameter, Constant.ZERO, new StackVariable(i)).writeTo(res);
    }
    returnAddress.writeTo(res);
    jump.writeTo(res);
    i = 0;
    for (Variable output : outputs) {
      i++;
      new AddOp(new StackVariable(i), Constant.ZERO, output).writeTo(res);
    }
  }
}
