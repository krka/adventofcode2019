public class ProgramAnalysis {
  private final int[] program;
  private final int[] states;

  private static final int OPCODE = 1;
  private static final int READ_VALUE = 2;
  private static final int WRITE_VALUE = 4;
  private static final int LABEL = 8;

  public ProgramAnalysis(int[] program) {
    this.program = program;
    this.states = new int[program.length];
  }

  public void markOpCode(int pc) {
    states[pc] |= OPCODE;
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
