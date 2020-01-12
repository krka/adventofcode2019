package intcode.assembler.parser;

import intcode.assembler.Assembler;

public interface Block {
  void finishBlock(Assembler assembler, Assembler.IntCodeFunction caller, String context);

  boolean breakBlock(Assembler assembler, Assembler.IntCodeFunction caller, String context);
}
