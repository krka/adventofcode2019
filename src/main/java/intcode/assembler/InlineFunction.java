package intcode.assembler;

import java.util.Set;

class InlineFunction implements Function {
  private final int numParams;
  private final int numReturnValues;
  private final InlineCode code;
  private final String name;

  public InlineFunction(String name, int numParams, int numReturnValues, InlineCode code) {
    this.name = name;
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
  public Set<String> getFunctionCalls() {
    return Set.of();
  }

  @Override
  public int finalize(int address) {
    return address;
  }

  @Override
  public void writeTo(AnnotatedIntCode res) {
  }

  @Override
  public int getNumReturnValues() {
    return numReturnValues;
  }

  @Override
  public String getName() {
    return name;
  }

  interface InlineCode {
    void call(Assembler.IntCodeFunction caller, String context, int returnVars);
  }
}
