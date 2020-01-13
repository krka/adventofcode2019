package intcode.assembler;

public class EqOp extends MathOp {
  public EqOp(String context, Parameter first, Parameter second, Parameter target) {
    super(8, context, first, second, target);
  }

  @Override
  protected String opname() {
    return "EQ";
  }
}
