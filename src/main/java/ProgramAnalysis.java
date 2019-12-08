public class ProgramAnalysis {
  private final int[] program;
  private final int[] states;

  private static final int OPCODE = 1;
  private static final int OPCODE_PARAM = 2;
  private static final int READ_VALUE = 4;
  private static final int WRITE_VALUE = 8;
  private static final int LABEL = 16;

  public ProgramAnalysis(int[] program) {
    this.program = program;
    this.states = new int[program.length];
  }

  public void markOpCode(int pc, int numParams) {
    states[pc] |= OPCODE;
    for (int i = 1; i <= numParams; i++) {
      states[pc + i] |= OPCODE_PARAM;
    }
  }

  public void markRead(int pc) {
    states[pc] |= READ_VALUE;
  }

  public void markWrite(int pc) {
    states[pc] |= WRITE_VALUE;
  }

  public void markLabel(int pc) {
    states[pc] |= LABEL;
  }

  public String toString(int i) {
    int state = states[i];
    StringBuilder sb = new StringBuilder();
    if ((state & OPCODE) != 0) {
      sb.append(" opcode");
    }
    if ((state & OPCODE_PARAM) != 0) {
      sb.append(" param");
    }
    if ((state & READ_VALUE) != 0) {
      sb.append(" read");
    }
    if ((state & WRITE_VALUE) != 0) {
      sb.append(" write");
    }
    if ((state & LABEL) != 0) {
      sb.append(" label");
    }
    return sb.toString();
  }
}
