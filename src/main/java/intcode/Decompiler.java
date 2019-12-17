package intcode;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Decompiler {
  private final List<BigInteger> program;
  private final Map<BigInteger, Note> notes = new HashMap<>();
  private final Memory memory;

  public Decompiler(List<BigInteger> program) {
    this.program = program;
    memory = new Memory(program);
  }

  public void decompile() {
    visitOpcode(BigInteger.ZERO);
  }

  public void print() {
    for (int i = 0; i < program.size(); i++) {
      Note note = notes.get(BigInteger.valueOf(i));
      if (note != null) {
        System.out.printf("%10d: %s%n", i, note);
      }
    }
  }

  private void visitOpcode(BigInteger address) {
    OpCodeEval prev = null;
    while (address != null) {
      Note note = notes.computeIfAbsent(address, ignore -> new Note());
      if (note.visited) {
        return;
      }
      note.visited = true;
      if (note.write) {
        //throw new RuntimeException("Address " + address + " is not allowed to be both write target and opcode");
      }

      OpCode opcode = OpCode.decode(memory, address);

      note.setOpcode(opcode);

      for (int i = 1; i < opcode.size(); i++) {
        Note paramNote = notes.computeIfAbsent(address.add(BigInteger.valueOf(i)), ignore -> new Note());
        if (paramNote.write) {
          // TODO: mark this some way?
        }
        paramNote.setOpcodeParam(opcode, i);
      }

      OpCodeEval eval = opcode.evaluate();
      BigInteger evalAddress = address;

      try {
        writeToMemory(eval.writeAddress);

        if ((eval.jumpTarget != null) && (prev != null) && BigInteger.ZERO.equals(prev.writeOffset)) {
          if (Objects.equals(address.add(OpCode.JumpIf.BIG_SIZE), prev.writeValue)) {
            setFunction(eval.jumpTarget);
            visitOpcode(eval.jumpTarget);
            address = prev.writeValue;
            prev = null;
          }
        }

        if (eval.relativeJumpTarget != null) {
          if (eval.relativeJumpTarget.equals(BigInteger.ZERO)) {
            setReturn(address);
            return;
          } else {
            throw new RuntimeException("Illegal relative jump target");
          }
        } else {
          prev = eval;
          if (eval.nextOperation == null) {
            setJumpTarget(eval.jumpTarget);
            address = eval.jumpTarget;
          } else {
            setJumpTarget(eval.jumpTarget);
            visitOpcode(eval.jumpTarget);
            address = eval.nextOperation;
          }
        }
      } finally {
        //System.out.printf("%10s: %s%n", evalAddress, note);
      }
    }
  }

  private void setReturn(BigInteger address) {
    notes.computeIfAbsent(address, ignore -> new Note()).returnStatement = true;
  }

  private void setFunction(BigInteger address) {
    if (address != null) {
      notes.computeIfAbsent(address, ignore -> new Note()).function = true;
    }
  }

  private void setJumpTarget(BigInteger address) {
    if (address != null) {
      notes.computeIfAbsent(address, ignore -> new Note()).jumpTarget = true;
    }
  }

  private void writeToMemory(BigInteger address) {
    if (address == null) {
      return;
    }
    Note note = notes.computeIfAbsent(address, ignore -> new Note());
    if (note.opcode != null && note.opcodeParam == 0) {
      //throw new RuntimeException("Address " + address + " is not allowed to be both write target and opcode");
    }
    // TODO: if writing to params?
    note.write = true;
  }

  private class Note {
    public boolean visited;
    public boolean function;
    public boolean returnStatement;
    private OpCode opcode;
    private int opcodeParam;
    private boolean write;
    private boolean jumpTarget;

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
          return typeStr() + opcode.toString();
        } else {
          return "param " + opcodeParam;
        }
      } else if (write) {
        return "memory";
      }
      return "<unused>";
    }

    private String typeStr() {
      if (function) {
        return "func   ";
      }
      if (jumpTarget) {
        return "label  ";
      }
      if (returnStatement) {
        return "return ";
      }
      return "       ";
    }

    public void setJumpTarget() {
      this.jumpTarget = true;
    }
  }
}
