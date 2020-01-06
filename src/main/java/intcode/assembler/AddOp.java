package intcode.assembler;

public class AddOp extends MathOp {
  public AddOp(String context, Parameter first, Parameter second, Parameter target) {
    super(1, context, first, second, target);
  }
}
