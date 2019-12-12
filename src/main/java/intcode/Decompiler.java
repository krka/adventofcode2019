package intcode;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decompiler {
  private final List<BigInteger> program;
  private final Map<BigInteger, Note> notes = new HashMap<>();
  private final Memory memory;

  public Decompiler(List<BigInteger> program) {
    this.program = program;
    memory = new Memory(program);
  }

  public void decompile() {
    visit(BigInteger.ZERO);
  }

  public void print() {
    for (int i = 0; i < program.size(); i++) {
      Note note = notes.get(BigInteger.valueOf(i));
      if (note != null) {
        System.out.printf("%d %s%n", note);
      }
    }
  }

  private void visit(BigInteger address) {
    if (address == null) {
      return;
    }
    Note note = notes.computeIfAbsent(address, ignore -> new Note());
    if (note.opcode != null) {
      return;
    }
    if (note.write) {
      throw new RuntimeException("Address " + address + " is not allowed to be both write target and opcode");
    }

    OpCode opcode = OpCode.decode(memory, address);

    note.setOpcode(opcode);
    //System.out.printf("%d %s%n", note);

    for (int i = 1; i < opcode.size(); i++) {
      Note paramNote = notes.computeIfAbsent(address.add(BigInteger.valueOf(i)), ignore -> new Note());
      if (paramNote.write) {
        throw new RuntimeException("Address " + address + " is not allowed to be both write target and opcode");
      }
      paramNote.setOpcodeParam(opcode, i);
    }

    OpCodeEval eval = opcode.evaluate();
    writeToMemory(eval.writeAddress);
    visit(eval.address1);
    visit(eval.address2);
  }

  private void writeToMemory(BigInteger address) {
    if (address == null) {
      return;
    }
    Note note = notes.computeIfAbsent(address, ignore -> new Note());
    if (note.opcode != null) {
      throw new RuntimeException("Address " + address + " is not allowed to be both write target and opcode");
    }
    note.write = true;
  }

  private class Note {
    private OpCode opcode;
    private int opcodeParam;
    private boolean write;

    public void setOpcode(OpCode opcode) {
      this.opcode = opcode;
      this.opcodeParam = 0;
    }

    public void setOpcodeParam(OpCode opcode, int i) {
      this.opcode = opcode;
      this.opcodeParam = i;
    }

    @Override
    public String toString() {
      if (opcode != null) {
        if (opcodeParam == 0) {
          return opcode.toString();
        } else {
          return "param " + opcodeParam;
        }
      } else if (write) {
        return "memory";
      }
      return "<unused>";
    }
  }
}
