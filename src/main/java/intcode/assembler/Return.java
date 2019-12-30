package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

public class Return extends Op {
  private final Assembler.Function function;
  private final List<Parameter> returnValues;
  private final List<Variable> tempSpace;

  public Return(Assembler.Function function, List<Parameter> returnValues, List<Variable> tempSpace) {
    this.function = function;
    this.returnValues = returnValues;
    this.tempSpace = tempSpace;
  }

  @Override
  public int size() {
    return SetRelBase.SIZE + Jump.SIZE + AddOp.SIZE * (2 * returnValues.size());
  }

  @Override
  public void writeTo(List<BigInteger> res) {
    int relBase = function.getRelBase();

    for (int i = 0; i < returnValues.size(); i++) {
      new AddOp(returnValues.get(i), Constant.ZERO, tempSpace.get(i)).writeTo(res);
    }
    for (int i = 0; i < returnValues.size(); i++) {
      new AddOp(tempSpace.get(i), Constant.ZERO, new StackVariable(1 + i - relBase)).writeTo(res);
    }

    new SetRelBase().setParameter(-relBase).writeTo(res);
    new Jump(false, Constant.ZERO, new StackVariable(0), null).writeTo(res);
  }
}
