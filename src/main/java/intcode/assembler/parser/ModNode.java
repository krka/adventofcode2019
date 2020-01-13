package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Parameter;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;

class ModNode implements ExprNode {
  private final ExprNode left;
  private final ExprNode right;

  private ModNode(ExprNode left, ExprNode right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public String toString() {
    return left + " % " + right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ModNode mulNode = (ModNode) o;
    return left.equals(mulNode.left) &&
            right.equals(mulNode.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  public static ExprNode create(ExprNode left, ExprNode right) {
    if (BigInteger.ZERO.equals(right.value())) {
      throw new RuntimeException("Can not divide by zero");
    }
    if (BigInteger.ZERO.equals(left.value())) {
      return IntConstant.ZERO;
    }
    if (BigInteger.ONE.equals(right.value())) {
      return left;
    }
    if (BigInteger.valueOf(-1).equals(right.value())) {
      return NegNode.create(left);
    }
    if (left.value() != null && right.value() != null) {
      return new IntConstant(left.value().divide(right.value()));
    }
    return new ModNode(left, right);
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    assembler.includeResource("division.asm");
    FunctionCallNode div = new FunctionCallNode("mod", new ExpressionList(left, right));
    div.assignTo(target, assembler, function, context);
  }
}
