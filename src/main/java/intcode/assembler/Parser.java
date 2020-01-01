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

    INSTRUCTIONS.add(new JumpEqInstruction());
    INSTRUCTIONS.add(new JumpNotEqInstruction());
    INSTRUCTIONS.add(new JumpLessThanInstruction());
    INSTRUCTIONS.add(new JumpLessThanEqInstruction());
    INSTRUCTIONS.add(new JumpGreaterThanInstruction());
    INSTRUCTIONS.add(new JumpGreaterThanEqInstruction());

    INSTRUCTIONS.add(new HaltInstruction());

    INSTRUCTIONS.add(new FunctionCallInstruction());
    INSTRUCTIONS.add(new NoReturnFunctionCallInstruction());

    INSTRUCTIONS.add(new FunctionDefinitionInstruction());
    INSTRUCTIONS.add(new EndFuncInstruction());
    INSTRUCTIONS.add(new ReturnInstruction());

    INSTRUCTIONS.add(new GetArrayInstruction());
    INSTRUCTIONS.add(new SetArrayInstruction());

  }

  public static boolean parse(String line, Assembler assembler, Assembler.Function function, String file, int lineNumber) {
    String context = file + ":" + lineNumber + "    " + line;
    for (Instruction instruction : INSTRUCTIONS) {
      if (instruction.apply(line, assembler, function, context)) {
        return true;
      }
    }
    return false;
  }
}
