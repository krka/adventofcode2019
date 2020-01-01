package intcode.assembler;

class AddOp extends MathOp {
  public AddOp(String context, Parameter first, Parameter second, Parameter target) {
    super(1, context, first, second, target);
  }
}
