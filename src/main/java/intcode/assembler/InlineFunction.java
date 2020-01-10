package intcode.assembler;

class InlineFunction implements Function {
  private final int numParams;
  private final int numReturnValues;
  private final InlineCode code;

  public InlineFunction(int numParams, int numReturnValues, InlineCode code) {
    this.numParams = numParams;
    this.numReturnValues = numReturnValues;
    this.code = code;
  }

  @Override
  public int getNumParameters() {
    return numParams;
  }

  @Override
  public void prepareCall(Assembler.IntCodeFunction caller, String context, int returnVars) {
    code.call(caller, context, returnVars);
  }

  @Override
  public int finalize(int address) {
    return address;
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
  }

  interface InlineCode {
    void call(Assembler.IntCodeFunction caller, String context, int returnVars);
  }
}
