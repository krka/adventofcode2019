package intcode.assembler;

public class LessThanOp extends MathOp {
  public LessThanOp(String context, Parameter first, Parameter second, Parameter target) {
    super(7, context, first, second, target);
  }

  @Override
  protected String opname() {
    return "LT";
  }
}
