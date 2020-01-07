package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Constant;
import intcode.assembler.Parameter;
import intcode.assembler.SetOp;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;

public class IntConstant implements ExprNode {
  public static final ExprNode ZERO = new IntConstant(BigInteger.ZERO);
  public static final ExprNode ONE = new IntConstant(BigInteger.ONE);

  private final BigInteger value;

  public IntConstant(BigInteger value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IntConstant that = (IntConstant) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public ExprNode optimize() {
    return this;
  }

  @Override
  public BigInteger value() {
    return value;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.Function function, String context) {
    function.operations.add(new SetOp(context, Constant.of(value), target));
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.Function function, Set<TempVariable> tempParams) {
    return Constant.of(value);
  }

}
