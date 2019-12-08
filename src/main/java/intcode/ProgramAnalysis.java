package intcode;

import java.util.Locale;

public class ProgramAnalysis {
  private final int[] program;
  private final int[] states;
  private final int[] numVisits;
  private final OpCode[] opcodes;

  private static final int OPCODE = 1;
  private static final int OPCODE_PARAM = 2;
  private static final int READ_VALUE = 4;
  private static final int WRITE_VALUE = 8;
  private static final int LABEL = 16;

  public ProgramAnalysis(int[] program) {
    this.program = program;
    this.states = new int[program.length];
    this.opcodes = new OpCode[program.length];
    this.numVisits = new int[program.length];
  }

  public void markOpCode(int pc, OpCode opCode) {
    states[pc] |= OPCODE;
    for (int i = 1; i <= opCode.size() - 1; i++) {
      states[pc + i] |= OPCODE_PARAM;
    }
    opcodes[pc] = opCode;
    numVisits[pc]++;
  }

  public void markRead(int address) {
    states[address] |= READ_VALUE;
  }

  public void markWrite(int address) {
    states[address] |= WRITE_VALUE;
  }

  public void markLabel(int address) {
    states[address] |= LABEL;
  }

  public String toString(int i) {
    int state = states[i];
    String param = (state & OPCODE_PARAM) != 0 ? "param": "";
    String read = (state & READ_VALUE) != 0 ? "r": "";
    String write = (state & WRITE_VALUE) != 0 ? "w": "";
    String label = (state & LABEL) != 0 ? "label": "";
    String opcode = (state & OPCODE) != 0 ? opcodes[i].name() + " " + opcodes[i].pretty(): "";
    return String.format(Locale.ROOT, "%05d  %10d   %5s %1s %1s %5s %s", i, program[i], param, read, write, label, opcode);
  }

  public String header() {
    return String.format(Locale.ROOT, "%5s  %10s   %5s %1s %1s %5s %-40s", "Addr", "Program", "Param", "R", "W", "Label", "Operation");
  }
}
