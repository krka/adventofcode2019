package intcode.assembler;

import java.util.List;

public class Return extends Op {
  private final Assembler.Function function;
  private final List<Parameter> returnValues;
  private final List<Variable> tempSpace;
  private final String context;

  public Return(Assembler.Function function, List<Parameter> returnValues, List<Variable> tempSpace, String context) {
    this.function = function;
    this.returnValues = returnValues;
    this.tempSpace = tempSpace;
    this.context = context;
  }

  @Override
  public int size() {
    return SetRelBase.SIZE + Jump.SIZE + AddOp.SIZE * (2 * returnValues.size());
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
    int relBase = function.getRelBase();

    for (int i = 0; i < returnValues.size(); i++) {
      new AddOp(context, returnValues.get(i), Constant.ZERO, tempSpace.get(i)).writeTo(res);
    }
    for (int i = 0; i < returnValues.size(); i++) {
      new AddOp(context, tempSpace.get(i), Constant.ZERO, new StackVariable(1 + i - relBase)).writeTo(res);
    }

    new SetRelBase(context).setParameter(-relBase).writeTo(res);
    new Jump("--", false, Constant.ZERO, new StackVariable(0), null).writeTo(res);
  }
}
