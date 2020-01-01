package intcode.assembler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedIntCode {
  private final List<AnnotatedOperation> operations = new ArrayList<>();
  private int pc = 0;
  private String nextLabel;

  public List<BigInteger> getIntCode() {
    ArrayList<BigInteger> res = new ArrayList<>();
    for (AnnotatedOperation operation : operations) {
      res.addAll(operation.getIntCode());
    }
    return res;
  }

  public int pc() {
    return pc;
  }

  public void addOperation(AnnotatedOperation annotatedOperation) {
    if (nextLabel != null) {
      annotatedOperation.setLabel(nextLabel);
      nextLabel = null;
    }
    annotatedOperation.setAddress(pc);
    operations.add(annotatedOperation);
    pc += annotatedOperation.size();
  }

  public void nextHasLabel(String label) {
    nextLabel = label;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (AnnotatedOperation operation : operations) {
      sb.append(operation.toString());
      sb.append('\n');
    }
    return sb.toString();
  }
}
