package intcode.assembler;

public class SetOp extends MathOp {
  public SetOp(String context, Parameter source, Parameter target) {
    super(1, context, source, Constant.ZERO, target);
  }
}
