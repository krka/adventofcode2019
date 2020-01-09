package intcode.assembler;

import java.util.ArrayList;
import java.util.List;

public class Parser {
  private static final List<Instruction> INSTRUCTIONS = new ArrayList<>();
  static {
    INSTRUCTIONS.add(new IncludeResource());
    INSTRUCTIONS.add(new Comment());
    INSTRUCTIONS.add(new LabelInstruction());
    INSTRUCTIONS.add(new DeclareInt());
    INSTRUCTIONS.add(new DeclareString());
    INSTRUCTIONS.add(new DeclareArray());

    INSTRUCTIONS.add(new JumpAlwaysInstruction());

    INSTRUCTIONS.add(new FunctionDefinitionInstruction());
    INSTRUCTIONS.add(new EndFuncInstruction());
  }

  public static boolean parse(String line, Assembler assembler, Assembler.Function function, String context) {
    for (Instruction instruction : INSTRUCTIONS) {
      if (instruction.apply(line, assembler, function, context)) {
        return true;
      }
    }
    return false;
  }
}
