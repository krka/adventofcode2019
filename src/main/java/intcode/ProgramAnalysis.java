package intcode;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProgramAnalysis {
  private final Memory memory;
  private final Map<Integer, Integer> states = new HashMap<>();
  private final Map<Integer, OpCode> opcodes = new HashMap<>();

  private static final int OPCODE = 1;
  private static final int OPCODE_PARAM = 2;
  private static final int READ_VALUE = 4;
  private static final int WRITE_VALUE = 8;
  private static final int LABEL = 16;

  public ProgramAnalysis(Memory memory) {
    this.memory = memory;
  }

  private void or(Map<Integer, Integer> map, int address, int value) {
    map.put(address, map.getOrDefault(address, 0) | value);
  }

  public void markOpCode(int pc, OpCode opCode) {
    or(states, pc, OPCODE);
    for (int i = 1; i <= opCode.size() - 1; i++) {
      or(states, pc + 1, OPCODE_PARAM);
    }
    opcodes.put(pc, opCode);
  }

  public void markRead(int address) {
    or(states, address, READ_VALUE);
  }

  public void markWrite(int address) {
    or(states, address, WRITE_VALUE);
  }

  public void markLabel(int address) {
    or(states, address, LABEL);
  }

  public String toString(int i) {
    int state = states.getOrDefault(i, 0);
    String param = (state & OPCODE_PARAM) != 0 ? "param": "";
    String read = (state & READ_VALUE) != 0 ? "r": "";
    String write = (state & WRITE_VALUE) != 0 ? "w": "";
    String label = (state & LABEL) != 0 ? "label": "";
    OpCode opCode = opcodes.get(i);
    String opcode = (state & OPCODE) != 0 ? opCode.name() + " " + opCode.pretty(): "";
    return String.format(Locale.ROOT, "%05d  %10d   %5s %1s %1s %5s %s", i, memory.read(i), param, read, write, label, opcode);
  }

  public String header() {
    return String.format(Locale.ROOT, "%5s  %10s   %5s %1s %1s %5s %-40s", "Addr", "Program", "Param", "R", "W", "Label", "Operation");
  }

  void printAnalysis(PrintWriter writer) {
    writer.println(header());
    for (int i = 0; i < memory.end; i++) {
      if (memory.contains(i)) {
        writer.println(toString(i));
      }
    }
  }
}
