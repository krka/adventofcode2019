package intcode.assembler;

public class ReturnValidation {
  private final Assembler.IntCodeFunction caller;
  private final Function target;
  private final int wantedReturnValues;
  private final String context;

  public ReturnValidation(Assembler.IntCodeFunction caller, Function target, int wantedReturnValues, String context) {
    this.caller = caller;
    this.target = target;
    this.wantedReturnValues = wantedReturnValues;
    this.context = context;
  }

  void validate() {
    if (target.getNumReturnValues() == -1) {
      throw new RuntimeException("Function " + target.getName() + " has not been fully parsed");
    }
    if (target.getNumReturnValues() < wantedReturnValues) {
      throw new RuntimeException(context + ": function " + target.getName() + " returns " + target.getNumReturnValues() + "values but caller wants " + wantedReturnValues + " values");
    }
  }
}
