package intcode.assembler;

import java.util.ArrayList;
import java.util.List;

public class Parser {
  private static final List<Instruction> INSTRUCTIONS = new ArrayList<>();
  static {
    INSTRUCTIONS.add(new IncludeResource());
    INSTRUCTIONS.add(new Comment());
    INSTRUCTIONS.add(new DeclareInt());
    INSTRUCTIONS.add(new DeclareString());
    INSTRUCTIONS.add(new DeclareArray());
    INSTRUCTIONS.add(new AddInstruction());
    INSTRUCTIONS.add(new MulInstruction());
    INSTRUCTIONS.add(new SetInstruction());
  }

  public static boolean parse(String line, Assembler assembler, Assembler.Function function) {
    for (Instruction instruction : INSTRUCTIONS) {
      if (instruction.apply(line, assembler, function)) {
        return true;
      }
    }
    return false;
  }
}
