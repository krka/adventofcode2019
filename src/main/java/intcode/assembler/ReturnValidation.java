package intcode.assembler;

public class ReturnValidation {
  private final Assembler.IntCodeFunction caller;
  private final Assembler.IntCodeFunction target;
  private final int wantedReturnValues;
  private final String context;

  public ReturnValidation(Assembler.IntCodeFunction caller, Assembler.IntCodeFunction target, int wantedReturnValues, String context) {
    this.caller = caller;
    this.target = target;
    this.wantedReturnValues = wantedReturnValues;
    this.context = context;
  }

  void validate() {
    if (target.numReturnValues == -1) {
      throw new RuntimeException("Function " + target.name + " has not been fully parsed");
    }
    if (target.numReturnValues < wantedReturnValues) {
      throw new RuntimeException(context + ": function " + target.name + " returns " + target.numReturnValues + " but caller wants " + wantedReturnValues);
    }
  }
}
