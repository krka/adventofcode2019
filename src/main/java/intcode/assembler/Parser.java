package intcode.assembler;

import java.util.ArrayList;
import java.util.List;

public class Parser {
  private static final List<Instruction> INSTRUCTIONS = new ArrayList<>();
  static {
    INSTRUCTIONS.add(new IncludeResource());
    INSTRUCTIONS.add(new Comment());
    INSTRUCTIONS.add(new LabelInstruction());
    INSTRUCTIONS.add(new InputInstruction());
    INSTRUCTIONS.add(new OutputInstruction());
    INSTRUCTIONS.add(new DeclareInt());
    INSTRUCTIONS.add(new DeclareString());
    INSTRUCTIONS.add(new DeclareArray());
    INSTRUCTIONS.add(new AddInstruction());
    INSTRUCTIONS.add(new MulInstruction());
    INSTRUCTIONS.add(new SetInstruction());
    INSTRUCTIONS.add(new EqualsInstruction());
    INSTRUCTIONS.add(new LessThanInstruction());
    INSTRUCTIONS.add(new GreaterThanInstruction());
    INSTRUCTIONS.add(new JumpFalseInstruction());
    INSTRUCTIONS.add(new JumpTrueInstruction());
    INSTRUCTIONS.add(new JumpAlwaysInstruction());
    INSTRUCTIONS.add(new HaltInstruction());
    INSTRUCTIONS.add(new FunctionCallInstruction());
    INSTRUCTIONS.add(new NoReturnFunctionCallInstruction());
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
