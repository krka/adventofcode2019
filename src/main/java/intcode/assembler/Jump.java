package intcode.assembler;

import java.math.BigInteger;

public class Jump extends Op {
  static final int SIZE = 3;
  private final String context;
  private final boolean isTrue;
  private final Parameter parameter;
  private final Parameter target;
  private final Label label;

  public Jump(String context, boolean isTrue, Parameter parameter, Parameter target, Label label) {
    this.context = context;
    this.isTrue = isTrue;
    this.parameter = parameter;
    this.target = target;
    this.label = label;
  }

  public static Jump toLabel(String context, Label label) {
    return new Jump(context, false, Constant.ZERO, null, label);
  }

  public static Jump toTarget(String context, Parameter target) {
    return new Jump(context, false, Constant.ZERO, target, null);
  }

  @Override
  public int size() {
    return SIZE;
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
    int opcode = isTrue ? 5 : 6;
    Parameter targetAddress = target == null ? label.getLabelAddress() : target;
    opcode += 100 * parameter.mode().ordinal() + 1000 * targetAddress.mode().ordinal();
    res.addOperation(new AnnotatedOperation(toString(), context, BigInteger.valueOf(opcode), parameter.value(), targetAddress.value()));
  }

  @Override
  public String toString() {
    String jumpTarget = target == null ? label.toString() : target.toString();
    return "JMP" + (isTrue ? "T" : "F") + " " + parameter + " -> " + jumpTarget;
  }
}
