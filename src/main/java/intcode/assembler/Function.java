package intcode.assembler;

import java.util.Set;

public interface Function {
  int getNumParameters();

  void prepareCall(Assembler.IntCodeFunction caller, String context, int returnVars);

  Set<String> getFunctionCalls();

  int finalize(int address);

  void writeTo(AnnotatedIntCode res);

  int getNumReturnValues();

  String getName();
}
