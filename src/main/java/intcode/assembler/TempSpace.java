package intcode.assembler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TempSpace {

  // Only enable to figure out who is not releasing variables
  private static final boolean DEBUG = false;

  private final Set<TempVariable> allVariables = new HashSet<>();
  private final List<TempVariable> ordering = new ArrayList<>();
  private final Set<TempVariable> available = new HashSet<>();

  private final String prefix;
  private int counter = 0;

  public TempSpace(String prefix) {
    this.prefix = prefix;
  }

  public TempVariable getAny() {
    if (!available.isEmpty()) {
      TempVariable variable = available.iterator().next();
      available.remove(variable);
      if (DEBUG) {
        variable.setCreatedBy(new Throwable());
      }
      return variable;
    }

    TempVariable variable = new TempVariable(prefix + counter, this);
    counter++;
    allVariables.add(variable);
    ordering.add(variable);
    if (DEBUG) {
      variable.setCreatedBy(new Throwable());
    }
    return variable;
  }

  public void release(TempVariable variable) {
    if (variable == null) {
      return;
    }
    if (!allVariables.contains(variable)) {
      throw new RuntimeException("Can't release unrelated variable");
    }
    if (!available.add(variable)) {
      throw new RuntimeException("Already released!");
    }
    variable.setCreatedBy(null);
  }

  public List<TempVariable> getAll() {
    if (available.size() != ordering.size()) {
      RuntimeException exception = new RuntimeException("All variables have not been released");
      for (TempVariable tempVariable : ordering) {
        if (tempVariable.getThrowable() != null) {
          exception.addSuppressed(tempVariable.getThrowable());
        }
      }
      throw exception;
    }
    return ordering;
  }
}
