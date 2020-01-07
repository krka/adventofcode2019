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

    INSTRUCTIONS.add(new JumpFalseInstruction());
    INSTRUCTIONS.add(new JumpTrueInstruction());
    INSTRUCTIONS.add(new JumpAlwaysInstruction());

    INSTRUCTIONS.add(new JumpEqInstruction());
    INSTRUCTIONS.add(new JumpNotEqInstruction());
    INSTRUCTIONS.add(new JumpLessThanInstruction());
    INSTRUCTIONS.add(new JumpLessThanEqInstruction());
    INSTRUCTIONS.add(new JumpGreaterThanInstruction());
    INSTRUCTIONS.add(new JumpGreaterThanEqInstruction());

    INSTRUCTIONS.add(new FunctionDefinitionInstruction());
    INSTRUCTIONS.add(new EndFuncInstruction());
    INSTRUCTIONS.add(new ReturnInstruction());
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
