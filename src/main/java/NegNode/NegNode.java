package NegNode;

import intcode.IntCode;
import intcode.assembler.Assembler;
import intcode.assembler.Constant;
import intcode.assembler.MulOp;
import intcode.assembler.Parameter;
import intcode.assembler.Variable;
import intcode.assembler.parser.ExprNode;
import intcode.assembler.parser.IntConstant;

import java.math.BigInteger;
import java.util.Objects;

public class NegNode implements ExprNode {
  private final ExprNode child;

  public NegNode(ExprNode child) {
    this.child = child;
  }

  @Override
  public String toString() {
    return "-" + child;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NegNode negNode = (NegNode) o;
    return child.equals(negNode.child);
  }

  @Override
  public int hashCode() {
    return Objects.hash(child);
  }

  @Override
  public ExprNode optimize() {
    ExprNode newChild = child.optimize();
    if (newChild instanceof IntConstant) {
      IntConstant child2 = (IntConstant) child;
      return new IntConstant(child2.value().negate());
    }
    return new NegNode(newChild);
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.Function function, String context) {
    Parameter parameter = child.asParameter(function);
    if (parameter != null) {
      function.operations.add(new MulOp(context, parameter, Constant.MINUS_ONE, target));
    } else {
      Variable temp = assembler.tempSpace.getAny();
      child.assignTo(temp, assembler, function, "# " + temp.getName() + " = " + child.toString());
      function.operations.add(new MulOp(context, temp, Constant.MINUS_ONE, target));
      assembler.tempSpace.release(temp);
    }
  }

  @Override
  public Parameter asParameter(Assembler.Function function) {
    return null;
  }
}
