package intcode.assembler;

public interface Function {
  int getNumParameters();

  void prepareCall(Assembler.IntCodeFunction caller, String context, int returnVars);

  int finalize(int address);

  void writeTo(AnnotatedIntCode res);

  int getNumReturnValues();

  String getName();
}
