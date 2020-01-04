package intcode.assembler;

public class ReturnValidation {
  private final Assembler.Function function;
  private final int wantedReturnValues;

  public ReturnValidation(Assembler.Function function, int wantedReturnValues) {
    this.function = function;
    this.wantedReturnValues = wantedReturnValues;
  }

  void validate() {
    if (function.numReturnValues == -1) {
      throw new RuntimeException("Function " + function.name + " has not been fully parsed");
    }
    if (function.numReturnValues < wantedReturnValues) {
      throw new RuntimeException("Function " + function.name + " returns " + function.numReturnValues + " but caller wants " + wantedReturnValues);
    }
  }
}
