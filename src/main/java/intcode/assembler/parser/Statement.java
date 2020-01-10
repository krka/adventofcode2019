package intcode.assembler.parser;

import intcode.assembler.Assembler;

public interface Statement {
  void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context);
}
