package intcode.assembler.parser;

import intcode.assembler.Assembler;

public interface Statement {
  void apply(Assembler assembler, Assembler.Function function, String context);
}
