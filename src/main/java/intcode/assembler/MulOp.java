package intcode.assembler;

public class MulOp extends MathOp {
  public MulOp(String context, Parameter first, Parameter second, Parameter target) {
    super(2, context, first, second, target);
  }

  @Override
  protected String opname() {
    return "MUL";
  }
}
