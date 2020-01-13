package intcode.assembler;

import java.math.BigInteger;

abstract class MathOp extends Op {
  static final int SIZE = 4;
  private final String context;
  protected final Parameter first;
  private final Parameter second;
  private final int opCode;
  protected Parameter target;


  public MathOp(int opCode, String context, Parameter first, Parameter second, Parameter target) {
    this.opCode = opCode;
    this.context = context;
    this.first = first;
    this.second = second;
    this.target = target;
    target.assertNotImmediate();
  }

  public void setTarget(Parameter target) {
    this.target = target;
    target.assertNotImmediate();
  }

  @Override
  public int size() {
    return SIZE;
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
    int opcode = opCode + 100 * first.mode().ordinal() + 1000 * second.mode().ordinal() + 10000 * target.mode().ordinal();
    res.addOperation(new AnnotatedOperation(toString(), context, BigInteger.valueOf(opcode), first.value(), second.value(), target.value()));
  }

  @Override
  public String toString() {
    return opname() + " " + first + " " + second + " -> " + target;
  }

  protected abstract String opname();
}
